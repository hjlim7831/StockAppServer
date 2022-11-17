package com.user.stock.holding;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user/stock")
public class UserStockHoldingController {
	
	@Autowired
	UserStockHoldingService userStockHoldingService;
	
	/**
	 * sorting_method에 따른 사용자의 보유 주식 조회하기
	 * @param sorting_method ("all", "cnt_desc", "rate_desc", "rate_asc", "price_desc")
	 * **/
	@ApiOperation(value = "사용자의 보유 주식을 정렬 방식에 따라 조회")
	@GetMapping("holding/{sorting_method}")
	public Map<String, Object> lookupUserStock(@PathVariable String sorting_method) {
		return userStockHoldingService.lookupUserStock(sorting_method);
	}
	
	/**
	 * stock_code 종목에 대한 사용자의 보유 현황 조회하기
	 * @param stock_code (숫자 6자리)
	 * **/
	@ApiOperation(value = "사용자의 해당 주식 종목 보유량을 조회")
	@GetMapping("holding/{stock_code}/check") // 사용자의 stock_code 종목 보유 현황 조회하기 
	public Map<String, Object> lookupUserStockOne(@PathVariable String stock_code) {
		return userStockHoldingService.lookupUserStockOne(stock_code);
	}
}