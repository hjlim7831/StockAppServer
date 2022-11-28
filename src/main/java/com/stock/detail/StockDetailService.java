
package com.stock.detail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.news.JsoupNewsComponent;
import com.data.stock.crawling.news.NewsDto;
import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimePriceDto;
import com.stock.detail.dto.StockDataDto;
import com.stock.detail.dto.StockInfoDto;
import com.stock.detail.dto.StockRelationDto;


@Service
public class StockDetailService {

	@Autowired
	JsoupNewsComponent jsoupNewsComponent;

	@Autowired
	RealtimeComponent realtimeComponent;
	
	@Autowired
	StockDetailMapper stockDetailMapper;

	public Map<String, Object> stockDetailInfo(String stock_code) {
		Map<String, Object> resultMap = new HashMap<>();
		
		StockInfoDto stockInfoDto = stockDetailMapper.selectStockByCode(stock_code);
		
		if (stockInfoDto == null) {
			resultMap.put("response", "failure_to_find_stock");
			resultMap.put("contents", "존재하지 않는 주식 종목입니다.");
		} else {
			resultMap.put("response", "success_lookup_stock_info");
			resultMap.put("contents", stockInfoDto);
		}
				
		return resultMap;
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
	
	public Map<String, Object> stockDetailGraph(String stock_code) {
		Map<String, Object> resultMap = new HashMap<>();

		List<StockDataDto> stockDataDtoList = stockDetailMapper.selectStockByDate(stock_code);
		
		if (stockDataDtoList == null) {
			resultMap.put("response", "failure_not_exist_stock");
			resultMap.put("contents", "존재하지 않는 주식 종목입니다.");
		} else {
			resultMap.put("response", "success_get_stock_graph_data");
			resultMap.put("contents", stockDataDtoList);
		}
		
		return resultMap;
	}
	
	public Map<String, Object> stockDetailRelations(String stock_code) {
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(".\\src\\main\\resources\\relations.json");	
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			JSONArray jsonArray = (JSONArray) jsonObject.get(stock_code);
			
			List<StockRelationDto> stockRelationsDtoList = new ArrayList<>();
			for (int i=0; i<jsonArray.size(); i++) {
				JSONObject relation = (JSONObject) jsonArray.get(i);
				
				long number = (long) relation.get("number");
				String code = (String) relation.get("code");
				String name = (String) relation.get("name");
				
				stockRelationsDtoList.add(new StockRelationDto(number, code, name));
			}
			
			if (stockRelationsDtoList.size() < 1) {
				resultMap.put("response", "success_get_no_relations");
				resultMap.put("contents", stockRelationsDtoList);
			} else {
				resultMap.put("response", "success_get_relations");
				resultMap.put("contents", stockRelationsDtoList);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}

	public Map<String, Object> stockDetailNews(String stock_code) {
		Map<String, Object> resultMap = new HashMap<>();
		List<NewsDto> contents = jsoupNewsComponent.getNewsList(stock_code);
		String response = String.format("success_get_news_list_of_%s",stock_code);
		resultMap.put("contents", contents);
		resultMap.put("response", response);
		
		return resultMap;
	}

}