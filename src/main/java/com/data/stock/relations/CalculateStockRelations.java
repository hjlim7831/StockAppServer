package com.data.stock.relations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stock.detail.StockDetailMapper;
import com.stock.detail.dto.StockDto;

@Component
public class CalculateStockRelations {
	// stockDto를 자카드 유사도로 재정렬하기 위한 wrapper class 생성
	class StockWrapper implements Comparable<StockWrapper> {
		StockDto stockDto;
		double similarity;

		public StockWrapper() {

		}

		public StockWrapper(StockDto stockDto, double similarity) {
			this.stockDto = stockDto;
			this.similarity = similarity;
		}

		// 내림차순 정렬
		@Override
		public int compareTo(StockWrapper o) {
			return Double.compare(o.similarity, this.similarity);
		}

	}

	@Autowired
	StockDetailMapper stockDetailMapper;

	public void findStockRelations() {
		List<StockDto> stockList = stockDetailMapper.selectAllStock();

		JsonObject jsonObject = new JsonObject();

		int listSize = stockList.size();

		for (int i = 0; i < listSize; i++) {
			StockDto targetStock = stockList.get(i);
			String targetStock_code = targetStock.getStock_code();
			String targetStockCategory = targetStock.getCategory();

			// Category 같은 주식들 쭉 뽑기
			List<StockDto> sameCategoryStockList = new ArrayList<>();
			for (int j = 0; j < listSize; j++) {
				StockDto compStock = stockList.get(j);

				// 같은 주식이면 continue 하기
				if (targetStock_code.equals(compStock.getStock_code())) {
					continue;
				}

				if (targetStockCategory.equals(compStock.getCategory())) {
					sameCategoryStockList.add(compStock);
				}
			}

			int sSize = sameCategoryStockList.size();
			// Category 같은 애들이 2개 이하면 그냥 출력
			if (sSize <= 2) {
				jsonObject.add(targetStock_code, putListToJson(sameCategoryStockList));
			} else {
				StockWrapper[] wrapList = new StockWrapper[sSize];

				// 2개 이상이면 이제 프로덕트로 유사도 계산해서 상위 두개 보여주기
				String[] targetStockProducts = targetStock.getProduct().replace(" ", "").split(",");
				for (int j = 0; j < sSize; j++) {
					StockDto compStock = sameCategoryStockList.get(j);
					
					// 나누는 기준 , / ( )
					String[] compStockProducts = compStock.getProduct().replace(" ", "").split(",");

					double similarity = jaccardSimilarity(targetStockProducts, compStockProducts);
					wrapList[i] = new StockWrapper(compStock, similarity);

				}
				Arrays.sort(wrapList);

				List<StockDto> resList = new ArrayList<>();
				for (int j = 0; j < 2; j++) {
					resList.add(wrapList[j].stockDto);
				}
				
				jsonObject.add(targetStock_code, putListToJson(resList));

			}

		}
		System.out.println(jsonObject.toString());
		
		
		
		
	}

	private double jaccardSimilarity(String[] strArr1, String[] strArr2) {
		int len1 = strArr1.length;
		int len2 = strArr2.length;

		if (len1 == 0 || len2 == 0) {
			return 0;
		}

		Map<String, Integer> map1 = new HashMap<>();
		Map<String, Integer> map2 = new HashMap<>();

		for (int i = 0; i < len1; i++) {
			String target = strArr1[i];
			if (map1.containsKey(target)) {
				int tmpNum = map1.get(target);
				map1.put(target, tmpNum + 1);
			} else {
				map1.put(target, 1);
			}
		}

		for (int i = 0; i < len2; i++) {
			String target = strArr2[i];
			if (map2.containsKey(target)) {
				int tmpNum = map2.get(target);
				map2.put(target, tmpNum + 1);
			} else {
				map2.put(target, 1);
			}
		}

		Set<String> set1 = map1.keySet();
		Set<String> set2 = map2.keySet();

		double sub = 0;
		double uni = 0;
		for (String c : set1) {
			if (set2.contains(c)) {
				sub += Math.min(map1.get(c), map2.get(c));
			}
		}

		Set<String> union = new HashSet<>(set1);
		union.addAll(set2);

		for (String c : union) {
			int num1 = 0;
			int num2 = 0;
			if (set1.contains(c)) {
				num1 = map1.get(c);
			}
			if (set2.contains(c)) {
				num2 = map2.get(c);
			}
			uni += Math.max(num1, num2);
		}

		return sub / uni;
	}

	private JsonArray putListToJson(List<StockDto> list) {

		JsonArray jsonArray = new JsonArray();

		int listSize = list.size();
		for (int i = 0; i < listSize; i++) {
			JsonObject jsonObject = new JsonObject();
			StockDto stock = list.get(i);
			String companyName = stock.getCompany_name();
			String stockCode = stock.getStock_code();

			jsonObject.addProperty("number", i + 1);
			jsonObject.addProperty("name", companyName);
			jsonObject.addProperty("code", stockCode);
			jsonArray.add(jsonObject);
		}

		return jsonArray;

	}

}
