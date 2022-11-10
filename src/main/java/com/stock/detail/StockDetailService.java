
package com.stock.detail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.news.JsoupNewsComponent;
import com.data.stock.crawling.news.NewsDto;
import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimePriceDto;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stock.detail.dto.StockDto;


@Service
public class StockDetailService {

	@Autowired
	JsoupNewsComponent jsoupNewsComponent;

	@Autowired
	RealtimeComponent realtimeComponent;
	
	@Autowired
	StockDetailMapper stockDetailMapper;

	public StockDto stockDetailInfo(String stock_code) {
		return stockDetailMapper.selectStockByCode(stock_code);
	}

	public Map<String, Object> stockDetailRealtime(String stock_code) {
		RealtimePriceDto realtimePriceDto = realtimeComponent.getRealtimePrice(stock_code);
		
		Map<String, Object> resultMap = new HashMap<>();
		if (realtimePriceDto == null) {
			resultMap.put("response", "failure_to_find_stock");
			resultMap.put("contents", "존재하지 않는 주식 종목입니다.");
		} else {
			Map<String, Object> realtime = new HashMap<>();
			realtime.put("now", realtimePriceDto.getNow());
			realtime.put("diff", realtimePriceDto.getDiff());
			realtime.put("rate", realtimePriceDto.getRate());

			resultMap.put("response", "success_get_realtime_stock_info");
			resultMap.put("contents", realtime);
		}
		
		return resultMap;
	}
	
	public String stockDetailRelations(String stock_code) {
		Gson gson = new Gson();
		Reader reader;
		JsonElement res;
		try {
			reader = new FileReader(".\\src\\main\\resources\\relations.json");	
			JsonObject obj = gson.fromJson(reader, JsonObject.class);
			res = obj.get(stock_code);
			return res.toString();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public List<NewsDto> stockDetailNews(String stock_code) {
		return jsoupNewsComponent.getNewsList(stock_code);
	}



}