package com.stock.recommend;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
		
		if (userInfoSessionDto.getUser_num() == null || userInfoSessionDto == null) {
			contents = getPopularStock();

		} else {
			String user_num = userInfoSessionDto.getUser_num();
			
			Set<String> stockSet = new HashSet<>(stockRecommendMapper.selectUserStockHolding(user_num));
			stockSet.addAll(stockRecommendMapper.selectUserWishlist(user_num));
			
			if (stockSet.size() < 3) {
				contents = getPopularStock();
				
			} else { // 보유 및 관심 주식 3개 이상
				int numberOfUsers = stockRecommendMapper.selectNumberOfUsers();
				
				if (numberOfUsers < 10) { // 사용자 10명 미만 - 관련 주식 중 랜덤
					List<String> stockList = new ArrayList<>(stockSet);
					contents = getRelatedStock(stockList);
				}
			}		
		}
		
		resultMap.put("response", "success_get_recommend_stock");
		resultMap.put("contents", contents);

		return resultMap;

	}

	private Map<String, Object> getPopularStock() {
		
		Map<String, Object> realtime = new HashMap<String, Object>();
		
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
		
		String code = searchResultDto.getStock_code();
		
		RealtimePriceDto realtimePriceDto = realtimeComponent.getRealtimePrice(code);
		realtime.put("now", realtimePriceDto.getNow());
		realtime.put("diff", realtimePriceDto.getDiff());
		realtime.put("rate", realtimePriceDto.getRate());
		realtime.put("stock_code", code);
		realtime.put("name", searchResultDto.getCompany_name());
		
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

				String code = (String) relation.get("code");

				RealtimePriceDto realtimePriceDto = realtimeComponent.getRealtimePrice(code);
				realtime.put("now", realtimePriceDto.getNow());
				realtime.put("diff", realtimePriceDto.getDiff());
				realtime.put("rate", realtimePriceDto.getRate());
				realtime.put("stock_code", code);
				realtime.put("name", relation.get("name"));

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
	
	public void recommendStock() {
		
		String[] allStockCode = stockRecommendMapper.selectAllStock();
		System.out.println();
		
		// relation : String 주식 코드, Integer 평가값 0 1
		Map<String, Integer> relation = new HashMap<>();
		for (String code : allStockCode) {
			relation.put(code, 0);
		}
		
		// String 주식 코드 ( )
		Map<String, Map<String, Double>> oneStock = new HashMap<>();
		for (String code : allStockCode) {
			oneStock.put(code, new HashMap<String, Double>());
		}
		
		// String 사용자 고유번호 () 
		Map<String, Map<String, Integer>> users = new HashMap<>();
		
		List<String> allUserNum = stockRecommendMapper.selectAllUserNum();
		
		for (String num : allUserNum) {
			Map<String, Integer> copyMap = new HashMap<String, Integer>(relation);
			
			List<String> myStock = stockRecommendMapper.selectUserStock(num);
			System.out.println(myStock.toString());
			for (String code : myStock) {
				copyMap.put(code, 1);
			}
			System.out.println(copyMap.toString());
			users.put(num, copyMap);
		}
		
		//for (Entry<String, Map<String, Integer>> entrySet : users.entrySet()) {
        //    System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
        //}
		
		for (int i=0; i<allStockCode.length; i++) {
			String code1 = allStockCode[i];
			
			for (int j=i+1; j<allStockCode.length; j++) {
				String code2 = allStockCode[j];
				
				int AproductB = 0;
				int A = 0;
				int B = 0;
				
				for (String num : allUserNum) {
					
					int a0 = ((Map<String, Integer>) users.get(num)).get(code1);
					int b0 = ((Map<String, Integer>) users.get(num)).get(code2);
					//if (a0 > 0) System.out.println(code1 + " " + code2 + " a " + a0);
					//if (b0 > 0) System.out.println(code1 + " " + code2 + " b " + b0);
					
					AproductB += (a0*b0);
					A += a0;
					B += b0;
				}
				
				if ((A + B - AproductB) == 0) {
					((HashMap<String, Double>) oneStock.get(code1)).put(code2, (double) 0);
					((HashMap<String, Double>) oneStock.get(code2)).put(code1, (double) 0);
				} else {
					double tanimoto = AproductB/(A + B - AproductB);
//					System.out.println("오긴함?2 " + A + " " + B + " " + AproductB);
					//if(tanimoto > 0) {
					//	System.out.println(tanimoto);
					//}
					((HashMap<String, Double>) oneStock.get(code1)).put(code2, tanimoto);
					((HashMap<String, Double>) oneStock.get(code2)).put(code1, tanimoto);				
				}
			}		
		}
		
		for (Entry<String, Map<String, Double>> entrySet : oneStock.entrySet()) {
			if (entrySet.getKey().equals("207940")) {
				System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
				break;		
			}
        }
		
		String user_num = userInfoSessionDto.getUser_num();
		Map<String, Integer> myUserList = users.get(user_num);
		
		Map<String, Double> relations = new HashMap<>();
		for (String code : allStockCode) {
			relations.put(code, (double) 0);
		}
		
		for (Entry<String, Integer> entrySet : myUserList.entrySet()) {
			if (entrySet.getValue() == 1) {
				String stock_code = entrySet.getKey();
				
				for (Entry<String, Double> entrySet2 : oneStock.get(stock_code).entrySet()) {
					String code = entrySet2.getKey();
					double value = entrySet2.getValue();
					
					relations.put(code, relations.get(code) + value);
				}
			}
        }
		
        System.out.println();
        List<Map.Entry<String, Double>> entries = new ArrayList<>(relations.entrySet());
        entries.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        
        int count = 0;
        for (Entry<String, Double> entry : entries) {
            System.out.println("Key: " + entry.getKey() + ", " + "Value: " + entry.getValue());
            
            count++;
            if (count >= 7) break;
        }
	}
}
