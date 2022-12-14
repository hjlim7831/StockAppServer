package com.stock.recommend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimePriceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.result.SearchResultDto;
import com.search.viewCntRank.SearchViewCntRankMapper;
import com.user.info.UserInfoSessionDto;

@Service
public class StockRecommendService_SJY {

	@Resource // session에 저장해 둔 로그인 정보 가져오기
	private UserInfoSessionDto userInfoSessionDto;

	@Autowired
	private StockRecommendMapper_SJY stockRecommendMapper;

	@Autowired
	private RealtimeComponent realtimeComponent;
	
	@Autowired
	SearchViewCntRankMapper viewCntRankMapper;
	
	public Map<String, Object> getRecommendStock() {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> contents = new HashMap<>();
		
		// 로그아웃 상태라면 
		if (userInfoSessionDto.getUser_num() == null || userInfoSessionDto == null) {
			contents = getPopularStock();
		}
		
		// 로그인 상태인 경우
		else {
			
			// 로그인 정보 가져오기
			String user_num = userInfoSessionDto.getUser_num();
			
			// 사용자 보유 및 관심 주식 목록 DB에서 가져오기
			Set<String> stockSet = new HashSet<>(stockRecommendMapper.selectUserStock(user_num));
			
			// 만약 사용자의 관심 및 보유 주식이 3개 미만이라면 로그아웃 상태와 동일하게 처리
			if (stockSet.size() < 3) {
				contents = getPopularStock();
			}
			
			// 사용자의 관심 및 보유 주식이 3개 이상
			else {
				
				// 보유 및 관심 주식이 있는 사용자 수
				int userSize = stockRecommendMapper.selectUserHaveHoldingWish();
				
				// 보유 및 관심 주식으로 설정돼 있는 주식 수
				int stockSize = stockRecommendMapper.selectAllWishAndHolding();
				
				// 전체 사용자 수
				int numberOfUsers = stockRecommendMapper.selectNumberOfUsers();
				
				// 보유 및 관심 주식이 있는 사용자 수가 10명 미만, 보유 및 관심 주식 수가 10개 미만, 전체 사용자 수가 20명 미만 중 하나라도 충족한다면
				if (userSize < 10 || stockSize < 10 || numberOfUsers < 20) {
					
					// 보유 및 관심 주식 중에 관련 주식을 가져오기
					List<String> stockList = new ArrayList<>(stockSet);
					contents = getRelatedStock(stockList);
				}
				
				// 로그인 상태, 사용자 수, 보유 및 관심 주식 수 등의 모든 조건을 충족한다면
				else {
					List<Entry<String, Double>> similarityTop5 = getCollaborativeFiltering();
					
					// 만약 추천 주식으로 걸러낸 주식이 없다면
					if (similarityTop5 == null) {
						
						// 보유 및 관심 주식 중에 관련 주식을 가져오기
						List<String> stockList = new ArrayList<>(stockSet);
						contents = getRelatedStock(stockList);
					}
					
					// 추천 주식 5개가 있다면 그 중에서 랜덤으로 하나 선택하기
					else {
						
						Random random = new Random();
						
						int idx = random.nextInt(similarityTop5.size());
						Entry<String, Double> stock = similarityTop5.get(idx);
						String code = stock.getKey();
						
						contents = getRealtimePrice(code, stockRecommendMapper.selectCompanyName(code));
						
					}
				}
			}		
		}
		
		resultMap.put("response", "success_get_recommend_stock");
		resultMap.put("contents", contents);

		return resultMap;

	}

	private Map<String, Object> getPopularStock() {
		
		// 보유 및 관심 주식이 있는 사용자 수
		int userSize = stockRecommendMapper.selectUserHaveHoldingWish();
		List<SearchResultDto> searchResultDtoList;
		
		if (userSize < 10) {
			searchResultDtoList = viewCntRankMapper.selectViewCntTop5Stock();
		} else {
			searchResultDtoList = stockRecommendMapper.selectStockPopular();
		}
		Random random = new Random();
		
		int idx = random.nextInt(searchResultDtoList.size());
		SearchResultDto searchResultDto = searchResultDtoList.get(idx);
		Map<String, Object> realtime = getRealtimePrice(searchResultDto.getStock_code(), searchResultDto.getCompany_name());
		
		return realtime;
	}
	
	private Map<String, Object> getRealtimePrice(String stock_code, String name) {
		Map<String, Object> realtime = new HashMap<String, Object>();

		RealtimePriceDto realtimePriceDto = realtimeComponent.getRealtimePrice(stock_code);
		realtime.put("now", realtimePriceDto.getNow());
		realtime.put("diff", realtimePriceDto.getDiff());
		realtime.put("rate", realtimePriceDto.getRate());
		realtime.put("stock_code", stock_code);
		realtime.put("name", name);

		return realtime;

	}
	
	// 관련 주식 중 랜덤으로 가져오기
	private Map<String, Object> getRelatedStock(List<String> stockList) {
		
		Map<String, Object> realtime = new HashMap<String, Object>();

		Random random = new Random();
		JSONParser parser = new JSONParser();

		try {
			Reader reader = new FileReader(".\\src\\main\\resources\\relations.json");
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			int num = 0;
			while (num < stockList.size()) {
				int idx = random.nextInt(stockList.size());
				String stock_code = stockList.get(idx);

				JSONArray jsonArray = (JSONArray) jsonObject.get(stock_code);

				num++;
				if (jsonArray.size() == 0) {
					continue;
				}
				JSONObject relation = (JSONObject) jsonArray.get(0);
				realtime = getRealtimePrice((String) relation.get("code"), (String) relation.get("name"));
				
				break;
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return realtime;
	}
	
	// 콘텐츠 기반 협업 필터링
	private List<Entry<String, Double>> getCollaborativeFiltering() {
		
		// 사용자 정보 가져오기
		String user_num = userInfoSessionDto.getUser_num();
		
		// 가져온 사용자의 관심 또는 보유 주식
		Set<String> myStockArray = stockRecommendMapper.selectUserStock(user_num);
		
		// 유사도 계산 결과값을 넣을 Map 생성
		Map<String, Double> similarity = new HashMap<>();

		try {
			
			// json 파일 읽어오기
			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(".\\src\\main\\resources\\recommendStock.json");
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			
			// 내 보유 주식 전체를 반복하면서, 각 주식에 대한 다른 주식 유사도 가져오기
			for (String stock_code : myStockArray) {
				JSONObject row = (JSONObject) jsonObject.get(stock_code);
				Map<String, Double> map = new ObjectMapper().readValue(row.toJSONString(), Map.class);
				
				for (Entry<String, Double> entrySet : map.entrySet()) {
					
					String key = entrySet.getKey();
					if (myStockArray.contains(key)) continue;
					
					Double score = entrySet.getValue();
					if (similarity.get(key) != null) score += similarity.get(key);
					
					similarity.put(entrySet.getKey(), score);
			    }
			}
			
			// 이전까지 합을 구해서 넣어줬다면 합을 평균으로 바꿔주기
			for (Entry<String, Double> entrySet : similarity.entrySet()) {
				similarity.put(entrySet.getKey(), entrySet.getValue()/myStockArray.size());
			}
			
			// value 값이 큰 순으로 정렬하기
			List<Map.Entry<String, Double>> entries = new ArrayList<>(similarity.entrySet());
	        entries.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
	        
	        if (entries.get(4).getValue() <= 0) return null;
	        
	        return entries.subList(0, 5);
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void makeRecommendJson() {
		
		// DB에 존재하는 전체 주식 코드를 배열로 불러오기
		String[] allStockCode = stockRecommendMapper.selectAllStock();
		
		// DB에 존재하는 전체 사용자 고유 번호를 배열로 불러오기
		String[] allUserNum = stockRecommendMapper.selectAllUserNum();
		
		// evaluation : String 주식 코드, Integer 평가값 0 또는 1
		Map<String, Integer> evaluation = new HashMap<>();
		for (String code : allStockCode) evaluation.put(code, 0);
		
		// stocks : String 주식 코드, Map<String, Double> String 주식 코드, Double 타니모토 유사도
		Map<String, Map<String, Double>> stocks = new HashMap<>();
		for (String code : allStockCode) stocks.put(code, new HashMap<String, Double>());
		
		// users : String 사용자 고유번호 , Map<String, Integer> String 주식 코드, Integer 평가값 0 또는 1
		Map<String, Map<String, Integer>> users = new HashMap<>();
		
		// 전체 사용자 번호를 반복하며 users 행렬 완성하기
		for (String num : allUserNum) {
			
			// users에 넣어줄 Map<String, Integer>를 만들기
			Map<String, Integer> evaluationCopy = new HashMap<String, Integer>(evaluation);
			
			// num 사용자의 보유 또는 관심 주식에 있는 주식 목록을 배열로 불러오기
			Set<String> myStockArray = stockRecommendMapper.selectUserStock(num);
			
			// myStockArray를 전부 돌며, 여기 있는 주식 코드의 평가값은 1로 변경
			for (String code : myStockArray) evaluationCopy.put(code, 1);
			
			// num에 대해 만들어진 한 행을 users에 넣어준다.
			users.put(num, evaluationCopy);
		}
		
		// 테스트으로 출력문
		// for (Entry<String, Map<String, Integer>> entrySet : users.entrySet()) {
        //     System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
        // }
		
		// 주식 코드를 2개씩 전부 반복
		for (int i=0; i<allStockCode.length; i++) {
			String code1 = allStockCode[i];
			
			for (int j=i+1; j<allStockCode.length; j++) {
				String code2 = allStockCode[j];
				
				double AproductB = 0; // A*B 구하기
				double A = 0;         // A 구하기
				double B = 0;         // B 구하기
				
				// 사용자를 전부 반복하며 A*B, A, B에 값 더해주기
				for (String num : allUserNum) {
					
					int An = users.get(num).get(code1);
					int Bn = users.get(num).get(code2);
					
					AproductB += (An*Bn);
					A += An;
					B += Bn;
				}
				
				double tanimoto;
				
				// 만약 분모가 0이라면 계산이 불가능하므로 0을 넣어준다.
				if ((A + B - AproductB) == 0) tanimoto = 0;
				else tanimoto = AproductB/(A + B - AproductB);
				
				stocks.get(code1).put(code2, tanimoto);
				stocks.get(code2).put(code1, tanimoto);	
			}		
		}
		
		// 테스트용으로 출력
//		for (Entry<String, Map<String, Double>> entrySet : stocks.entrySet()) {
//			if (entrySet.getKey().equals("005930")) {
//				System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
//				break;		
//			}
//        }
		
		// 유사도를 내림차순으로 정렬해 상위 10개만을 json 파일에 넣어준다.
		Map<String, Map<String, Double>> jsonStocks = new HashMap<>();
		
		for (Entry<String, Map<String, Double>> entrySet : stocks.entrySet()) {
			
			String code = entrySet.getKey();
			Map<String, Double> row = entrySet.getValue();
			
			// keySet을 람다표현식을 사용해 정렬
			List<String> keySet = new ArrayList<>(row.keySet());
			keySet.sort((o1, o2) -> row.get(o2).compareTo(row.get(o1)));
			
			Map<String, Double> newRow = new HashMap<>();
			for (int i=0; i<10; i++) {
				String stock_code = keySet.get(i);
				newRow.put(stock_code, row.get(stock_code));
			}

			jsonStocks.put(code, newRow);
		}
		
		// json 파일로 저장하기
		try {
			JSONObject jsonObject = new JSONObject(jsonStocks);
			
			FileWriter file = new FileWriter(".\\src\\main\\resources\\recommendStock.json");
			
			file.write(jsonObject.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;

	}
}
