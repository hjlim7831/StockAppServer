package com.stock.debut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockDebutService {
	
	@Autowired
	StockDebutMapper stockDebutMapper;
	
	public Map<String, Object> lookupDebutStock() {
		
		// return해줄 결과값
		Map<String, Object> resultMap = new HashMap<>();
		
		// DB에서 상장 예정 주식을 모두 가져와 stockDebutDtoList에 저장 (먼저 상장되는 순으로 정렬된 상태)
		List<StockDebutDto> stockDebutDtoList = stockDebutMapper.selectAllDebutStock();
		
		// response와 contents 초기값을 상장 예정 주식이 하나 이상일 때로 설정
		String response = "success_lookup_debut_stock";
		List<StockDebutDtoOrdered> contents = new ArrayList<>();
		
		if (stockDebutDtoList.size() == 0) { // 상장 예정 주식이 0개인 경우
			response = "failure_no_debut_stock";
			contents = null;
		} else {
			// 초기값 설정 (stockDebutDtoList의 index 0 값을 초기값으로 저장)
			String before_date = stockDebutDtoList.get(0).getNew_listing_date();
			List<StockDebutDto> before_stock_list = new ArrayList<>();
			before_stock_list.add(stockDebutDtoList.get(0));
			
			for (int i=1; i<=stockDebutDtoList.size(); i++) {
				
				// i가 stockDebutDtoList의 인덱스 내이고, 이전 상장 예정 주식의 상장 날짜와 현재 i의 상장 예정 날짜가 동일하다면
				if (i < stockDebutDtoList.size() && stockDebutDtoList.get(i).getNew_listing_date().equals(before_date)) {
					
					// 만들어둔 List<StockDebutDto>에 i 인덱스에 있는 StockDebutDto를 저장
					before_stock_list.add(stockDebutDtoList.get(i));
				} else {
					
					// 다른 날짜이므로, 
					StockDebutDtoOrdered stockDebutDtoOrdered = new StockDebutDtoOrdered();
					stockDebutDtoOrdered.setDate(before_date);
					stockDebutDtoOrdered.setStocks(before_stock_list);
					contents.add(stockDebutDtoOrdered);
					
					if (i < stockDebutDtoList.size()) {
						StockDebutDto stockDebutDto = stockDebutDtoList.get(i);
						before_date = stockDebutDto.getNew_listing_date();
						before_stock_list = new ArrayList<>();
						before_stock_list.add(stockDebutDto);
					}
				}
			}
		}
		
		resultMap.put("response", response);
		resultMap.put("contents", contents);
		return resultMap;
	}
	
}
