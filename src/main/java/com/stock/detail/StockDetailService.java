package com.stock.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.detail.dto.StockDto;
import com.stock.detail.dto.StockNewsDto;
import com.stock.detail.dto.StockRelationsDto;

@Service
public class StockDetailService {
	
	@Autowired
	StockDetailMapper stockDetailMapper;
	
	public StockDto stockDetailInfo(String stock_code) {
		return stockDetailMapper.selectStockByCode(stock_code);
	}
	
	
	public StockRelationsDto stockDetailRelations (String stock_code) {
		return stockDetailMapper.selectRelationsByCode(stock_code);
	}
	
	public StockNewsDto stockDetailNews(String stock_code) {
		return stockDetailMapper.selectNewsByCode(stock_code);
	}
}