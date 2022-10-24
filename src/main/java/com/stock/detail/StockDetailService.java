package com.stock.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crawling.stock.news.JsoupNewsComponent;
import com.crawling.stock.news.NewsDto;
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
	
	
	public StockRelationsDto stockDetailRelations (String stock_code) {
		return stockDetailMapper.selectRelationsByCode(stock_code);
	}
	
	public List<NewsDto> stockDetailNews(String stock_code) {
		return jsoupNewsComponent.getNewsList(stock_code);
	}
	
}