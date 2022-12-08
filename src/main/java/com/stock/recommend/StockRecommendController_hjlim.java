package com.stock.recommend;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class StockRecommendController_hjlim {
	
	@Autowired
	StockRecommendService_hjlim stockRecommendService;
	
	@GetMapping("recommend")
	public Map<String, Object> getRecommendedStock(){
		return stockRecommendService.getRecommendStock();
	}	
	
}
