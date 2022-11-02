package com.user.stock.holding;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserStockHoldingController {
	
	@Autowired
	UserStockHoldingService userStockService;
	
	@GetMapping("stock") // 사용자의 보유 주식 조회하기
	public Map<String, Object> lookupUserStockAll() {
		return userStockService.lookupUserStockAll();
	}
	
	@GetMapping("stock/{stock_code}") // 사용자의 stock_code 종목 보유 현황 조회하기 
	public Map<String, Object> lookupUserStockOne(@PathVariable String stock_code) {
		return userStockService.lookupUserStockOne(stock_code);
	}
}