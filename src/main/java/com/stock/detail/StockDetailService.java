
package com.stock.detail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.news.JsoupNewsComponent;
import com.data.stock.crawling.news.NewsDto;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stock.detail.dto.StockDto;


@Service
public class StockDetailService {

	@Autowired
	JsoupNewsComponent jsoupNewsComponent;

	@Autowired
	StockDetailMapper stockDetailMapper;

	public StockDto stockDetailInfo(String stock_code) {
		return stockDetailMapper.selectStockByCode(stock_code);
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