package com.stock.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimePriceDto;
import com.stock.detail.dto.StockDto;

@Service
public class StockCategoryService {
	
	@Autowired
	StockCategoryMapper stockCategoryMapper;
	
	@Autowired
	RealtimeComponent realtimeComponent;
	
	public Object[] categoryStocks(String category_name){
		
		List<StockDto> stockList = stockCategoryMapper.selectCategoryStocks(category_name);
		
		// 주식의 실시간 값과 함께 가져오기. (2개 랜덤으로 가져오기)
		// stock_code, company_name, category만 가져옴
		int lisSiz = stockList.size();
		int ind1 = new Random().nextInt(lisSiz);
		int ind2 = ind1;
		while(ind2 == ind1) {
			ind2 = new Random().nextInt(lisSiz);
		}
		
		StockDto[] stockArr = new StockDto[2];
		stockArr[0] = stockList.get(ind1);
		stockArr[1] = stockList.get(ind2);
		
		String stock_code1 = stockArr[0].getStock_code();
		String stock_code2 = stockArr[1].getStock_code();
		
		RealtimePriceDto[] realArr = new RealtimePriceDto[2];
		
		realArr[0] = realtimeComponent.getRealtimePrice(stock_code1);
		realArr[1] = realtimeComponent.getRealtimePrice(stock_code2);
		
		// [{stock_code:,company_name:, now:, diff:, rate:}]
		Object[] resArr = new Object[2];
		
		for(int i=0;i<2;i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("stock_code", stockArr[i].getStock_code());
			map.put("company_name", stockArr[i].getCompany_name());
			map.put("now", realArr[i].getNow());
			map.put("diff", realArr[i].getDiff());
			map.put("rate", realArr[i].getRate());
			resArr[i] = map;
		}
		
		return resArr;
	}

}
