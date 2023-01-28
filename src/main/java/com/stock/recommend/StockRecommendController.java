package com.stock.recommend;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("stock")
@Api(tags = {"Main Controller"}, description = "주요 증권, 주식 정보 관련 API")
public class StockRecommendController {
	
	@Autowired
	StockRecommendService stockRecommendService;
	
	@GetMapping("recommend")
	@ApiOperation(value = "추천 주식 조회")
	public Map<String, Object> getRecommendedStock(){
		return stockRecommendService.getRecommendStock();
	}
	
}
