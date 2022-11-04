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
	StockTradeService stockTradeService;
	
	@PostMapping("{stock_code}/trade") // stock_code 매매
	public Map<String, Object> trade(@PathVariable String stock_code, StockTradeDto stockTradeDto) {
		stockTradeDto.setStock_code(stock_code); // URL로 전달받은 stock_code 넣어주기
		return stockTradeService.trade(stockTradeDto);
	}
	
	@GetMapping("{stock_code}/price") // stock_code 현재 주가
	public Map<String, Object> tradePrice(@PathVariable String stock_code) {
		return stockTradeService.tradePrice(stock_code);
	}
}