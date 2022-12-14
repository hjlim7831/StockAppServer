package com.currency.trade;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("currency")
@Api(tags = {"Currency Exchange Controller"}, description = "환전 관련 API")
public class CurrencyTradeController {
	
	@Autowired
	CurrencyTradeService currencyTradeService;
	
	@PostMapping("exchange/{country}")
	@ApiOperation(value = "원화  → 외화 환전하기")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "country", value = "외화명(달러 : USD, 엔화 : JPY, 유로 : EUR, 위안 : CNY)", required = true),
		@ApiImplicitParam(name = "total", value = "환전할 원화 금액", required = true)
	})
	public Map<String, Object> tradeCurrency(@PathVariable String country, int total) {
		return currencyTradeService.tradeCurrency(country, total);
	}
	
	@PutMapping("exchange/{country}")
	@ApiOperation(value = "외화  → 원화 환전하기")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "country", value = "외화명(달러 : USD, 엔화 : JPY, 유로 : EUR, 위안 : CNY)", required = true),
		@ApiImplicitParam(name = "total", value = "환전할 외화 금액", required = true)
	})
	public Map<String, Object> tradeWon(@PathVariable String country, int total) {
		return currencyTradeService.tradeWon(country, total);
	}
	
	@GetMapping("price/{country}")
	@ApiOperation(value = "외화 가격 조회하기")
	@ApiImplicitParam(name = "country", value = "외화명(달러 : USD, 엔화 : JPY, 유로 : EUR, 위안 : CNY)", required = true)
	public Map<String, Object> tradePrice(@PathVariable String country) {
		return currencyTradeService.tradePrice(country);
	}
}