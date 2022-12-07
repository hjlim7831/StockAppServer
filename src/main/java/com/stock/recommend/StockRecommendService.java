package com.stock.recommend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
public class StockRecommendService {

	@Resource // session에 저장해 둔 로그인 정보 가져오기
	private UserInfoSessionDto userInfoSessionDto;

	@Autowired
	private StockRecommendMapper stockRecommendMapper;

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
				List<String> stockList = new ArrayList<>(stockSet);

				if (numberOfUsers < 10) { // 사용자 10명 미만 - 관련 주식 중 랜덤
					contents = getRelatedStock(stockList);
				} else { // 사용자 10명 이상 - 아이템 기반 협업 필터링
					contents = getItemBasedStock(stockList);
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

		String code = searchResultDto.getStock_code();

		Map<String, Object> realtime = getRealtimePrice(code, searchResultDto.getCompany_name());

		return realtime;
	}

	// 관련 주식 중 랜덤으로 가져오기
	private Map<String, Object> getRelatedStock(List<String> stockList) {
		Map<String, Object> realtime = new HashMap<>();

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
				realtime = getRealtimePrice(code, (String) relation.get("name"));

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

	// 협업 필터링으로 계산한 추천 주식 가져오기
	private Map<String, Object> getItemBasedStock(List<String> stockList) {
		
		Random random = new Random();
		
		// 타니모토 계수로 계산해서, 일정 값 이상이고 보유하고 있지 않은 주식들 목록을 쭉 가져오기
		List<String> recCodeList = calculateTanimoto(stockList);
		
		int idx = random.nextInt(recCodeList.size());
		
		String selCode = recCodeList.get(idx);
		
		// getRealtimePrice로 실시간 값 가져오기
		Map<String, Object> content = getRealtimePrice(selCode, stockRecommendMapper.selectCompanyNameByStockCode(selCode));

		return content;

	}

	public List<String> calculateTanimoto(List<String> stockList) {
		MatrixInfo matrixInfo = makeStockUserMatrixInfo();

		Set<String> stockSet = new HashSet<>(stockList);

		List<Boolean[]> matrix = matrixInfo.getMatrix();
		double[] l2Arr = matrixInfo.getL2Arr();
		Map<String, Integer> codeMap = matrixInfo.getCodeMap();
		List<String> codeIdxList = matrixInfo.getCodeIdxList();

		List<String> recCodeList = new ArrayList<>();

		for (String baseStock : stockList) {
			// 각 주식에 대한 다른 주식들의 타니모토 계수 계산하기

			Integer codeIdx = codeMap.get(baseStock);
			Boolean[] baseArr = matrix.get(codeIdx);

			for (int i = 0; i < matrix.size(); i++) {
				// 본인은 제외하기
				if (i == codeIdx)
					continue;

				double AdotB = 0;
				Boolean[] compArr = matrix.get(i);
				for (int j = 0; j < baseArr.length; j++) {
					if (baseArr[j] != null && compArr[j] != null) {
						AdotB += 1;
					}
				}

				double tmpTani = AdotB / (l2Arr[codeIdx] + l2Arr[i] - AdotB);
				String code = codeIdxList.get(i);

				if (!stockSet.contains(code) && tmpTani >= 0.4) {
					recCodeList.add(code);
				}
			}
		}
		return recCodeList;

	}

	/**
	 * 각 주식에 대해 유저들이 그 주식에 관심을 갖거나 보유하고 있는지 여부를 어레이로 리턴
	 * 
	 * @return Stock - UserIntegerArray
	 */
	private MatrixInfo makeStockUserMatrixInfo() {
		List<SelectedStockDto> preList = stockRecommendMapper.selectStockCodeListHoldingWish();

		int numOfUsers = stockRecommendMapper.selectUserHaveHoldingWish();

		List<Boolean[]> SUMatrix = new ArrayList<>();

		Map<String, Integer> codeMap = new HashMap<>();
		List<String> codeIdxList = new ArrayList<>();
		Map<String, Integer> userMap = new HashMap<>();

		int cLen = 0;
		int uLen = 0;

		for (SelectedStockDto s : preList) {
			String stock_code = s.getStock_code();
			String user_num = s.getUser_num();

			int sIdx;
			int uIdx;

			if (codeMap.containsKey(stock_code)) {
				sIdx = codeMap.get(stock_code);
			} else {
				codeMap.put(stock_code, cLen);
				codeIdxList.add(stock_code);
				sIdx = cLen++;
				SUMatrix.add(new Boolean[numOfUsers]);
			}

			if (userMap.containsKey(user_num)) {
				uIdx = userMap.get(user_num);
			} else {
				userMap.put(user_num, uLen);
				uIdx = uLen++;
			}

			SUMatrix.get(sIdx)[uIdx] = true;
		}

		// calculate L2 Arr (각 어레이의 거듭제곱의 합. 미리 계산해, 그 값만 가져올 수 있도록 하기)
		double[] l2Arr = new double[cLen];
		for (int i = 0; i < cLen; i++) {
			Boolean[] arr = SUMatrix.get(i);
			for (Boolean j : arr) {
				if (j != null) {
					l2Arr[i] += 1;
				}
			}
		}

		MatrixInfo info = new MatrixInfo(SUMatrix, codeMap, codeIdxList, l2Arr);

		return info;

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

}
