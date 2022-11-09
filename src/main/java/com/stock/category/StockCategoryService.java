package com.stock.category;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.detail.dto.StockDto;

@Service
public class StockCategoryService {
	
	@Autowired
	StockCategoryMapper stockCategoryMapper;
	
	public Map<String,Object> categoryStocks(String category_name){
		
		List<StockDto> stockList = stockCategoryMapper.selectCategoryStocks(category_name);
		
		// 주식의 실시간 값과 함께 가져오기.
		
		
		return null;
	}
	

}
