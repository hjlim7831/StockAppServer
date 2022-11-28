package com.user.stock.holding;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user/stock")
public class UserStockHoldingController {
	
	@Autowired
	UserStockHoldingService userStockHoldingService;
	
	@GetMapping("holding")
	@ApiOperation(value = "사용자의 보유 주식 전체를 정렬 방식에 따라 조회")
	@ApiImplicitParam(name = "sorting_method", value = "정렬 방식 (all, cnt_desc, rate_desc, rate_asc, price_desc)", required = true)
	public Map<String, Object> lookupUserStock(String sorting_method) {
		return userStockHoldingService.lookupUserStock(sorting_method);
	}
	
	@GetMapping("holding/{stock_code}") // 사용자의 stock_code 종목 보유 현황 조회하기 
	@ApiOperation(value = "사용자의 stock_code 주식 보유량을 조회")
	@ApiImplicitParam(name = "stock_code", value = "주식 종목 코드 (숫자 6자리)", required = true)
	public Map<String, Object> lookupUserStockOne(@PathVariable String stock_code) {
		return userStockHoldingService.lookupUserStockOne(stock_code);
	}
}