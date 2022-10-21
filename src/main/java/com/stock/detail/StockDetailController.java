package com.stock.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.detail.dto.StockDto;
import com.stock.detail.dto.StockNewsDto;
import com.stock.detail.dto.StockRelationsDto;

@RestController
@RequestMapping("stock")
public class StockDetailController {
	
	@Autowired
	StockDetailService stockDetailService;
	
	@PostMapping("detail-info")
	public StockDto stockDetailInfo(String stock_code) {
		return stockDetailService.stockDetailInfo(stock_code);
	}
	
	@PostMapping("detail-relations")
	public StockRelationsDto stockDetailRelations(String stock_code) {
		return stockDetailService.stockDetailRelations(stock_code);
	}
	
	@PostMapping("detail-news")
	public StockNewsDto stockDetailNews(String stock_code) {
		return stockDetailService.stockDetailNews(stock_code);
	}
}