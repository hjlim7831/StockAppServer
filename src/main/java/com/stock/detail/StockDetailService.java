
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

	public StockRelationsDto stockDetailRelations (String stock_code) throws FileNotFoundException {
		
		Reader reader = new FileReader(".\\src\\main\\resources\\relations.json");
		Gson gson = new Gson();
		StockRelationsDto[] stockReltationsDto = gson.fromJson(reader, StockRelationsDto[].class);
		System.out.println(stockReltationsDto);
		return null;
	}
	
	public List<NewsDto> stockDetailNews(String stock_code) {
		return jsoupNewsComponent.getNewsList(stock_code);
	}

}