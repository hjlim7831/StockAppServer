package com.stock.trade;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("stock")
@Api(tags = {"Trade Controller"}, description = "주식 매매 관련 API")
public class StockTradeController {
	
	@Autowired
	StockTradeService stockTradeService;
	
	@PostMapping("trade/{stock_code}")
	@ApiOperation(value = "stock_code 매수")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "stock_code", value = "주식 종목 코드", required = true),
		@ApiImplicitParam(name = "share", value = "거래할 주식 수", dataType = "int", example = "0", required = true)
	})
	public Map<String, Object> tradeBuy(@PathVariable String stock_code, int share) {
		return stockTradeService.tradeBuy(stock_code, share);
	}
	
	@PutMapping("trade/{stock_code}")
	@ApiOperation(value = "stock_code 매도")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "stock_code", value = "주식 종목 코드", required = true),
		@ApiImplicitParam(name = "share", value = "거래할 주식 수", dataType = "int", example = "0", required = true)
	})
	public Map<String, Object> tradeSell(@PathVariable String stock_code, int share) {
		return stockTradeService.tradeSell(stock_code, share);
	}
	
	@GetMapping("price/{stock_code}")
	@ApiOperation(value = "stock_code의 현재 주가")
	@ApiImplicitParam(name = "stock_code", value = "주식 종목 코드", required = true)
	public Map<String, Object> tradePrice(@PathVariable String stock_code) throws JsonProcessingException {
		return stockTradeService.tradePrice(stock_code);
	}
	
	@GetMapping("is-close")
	@ApiOperation(value = "주식 개장 여부")
	public Map<String, Object> isClose(){
		return stockTradeService.isClose();
	}
}