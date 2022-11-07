
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
import com.stock.detail.dto.StockRelationsDto;

@Service
public class StockDetailService {

	@Autowired
	JsoupNewsComponent jsoupNewsComponent;

	@Autowired
	StockDetailMapper stockDetailMapper;

	public StockDto stockDetailInfo(String stock_code) {
		return stockDetailMapper.selectStockByCode(stock_code);
	}

	public StockRelationsDto stockDetailRelations(String stock_code) {
		Gson gson = new Gson();
		Reader reader;
		try {
			reader = new FileReader(".\\src\\main\\resources\\relations.json");	
			JsonObject obj = gson.fromJson(reader, JsonObject.class);
			JsonElement list = obj.get(stock_code);
			System.out.println(list);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<NewsDto> stockDetailNews(String stock_code) {
		return jsoupNewsComponent.getNewsList(stock_code);
	}


}