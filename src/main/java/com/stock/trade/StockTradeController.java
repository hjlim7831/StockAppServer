package com.stock.trade;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class StockTradeController {
	
	@Autowired
	StockTradeService userStockTradeService;
	
	@PostMapping("{stock_code}/trade") // stock_code 매매
	public Map<String, Object> trade(@PathVariable String stock_code, StockTradeDto userStockTradeDto) {
		userStockTradeDto.setStock_code(stock_code); // URL로 전달받은 stock_code 넣어주기
		return userStockTradeService.trade(userStockTradeDto);
	}
	
	@GetMapping("{stock_code}/price")
	public Map<String, Object> tradePrice(@PathVariable String stock_code) {
		return userStockTradeService.tradePrice(stock_code);
	}
}