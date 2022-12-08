package com.stock.recommend;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class StockRecommendController {
	
	@Autowired
	StockRecommendService stockRecommendService;
	
	@GetMapping("recommend")
	public Map<String, Object> getRecommendedStock(){
		return stockRecommendService.getRecommendStock();
	}
	
	@GetMapping("test")
	public void recommendStock() {
		stockRecommendService.recommendStock();
		return;
	}
	
}
