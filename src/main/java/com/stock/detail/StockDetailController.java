
package com.stock.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.detail.dto.StockDto;
import com.stock.detail.dto.StockRelationsDto;

@RestController
@RequestMapping("stock")
public class StockDetailController {

	@Autowired
	StockDetailService stockDetailService;

	@PostMapping("{stock_code}/info")
	public StockDto stockDetailInfo(@PathVariable String stock_code) {
		return stockDetailService.stockDetailInfo(stock_code);
	}

	@PostMapping("{stock_code}/relations")
	public StockRelationsDto stockDetailRelations(@PathVariable String stock_code) {
		return stockDetailService.stockDetailRelations(stock_code);
	}

//	@PostMapping("{stock_code}/news")
//	public List<NewsDto> stockDetailNews(@PathVariable String stock_code) {
//		return stockDetailService.stockDetailNews(stock_code);
//	}
}