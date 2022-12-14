package com.stock.recommend;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class StockRecommendController_SJY {
	
	@Autowired
	StockRecommendService_SJY stockRecommendService;
	
	@GetMapping("recommend")
	public Map<String, Object> getRecommendedStock(){
		return stockRecommendService.getRecommendStock();
	}
	
	@GetMapping("test")
	public void recommendStock() {
		stockRecommendService.makeRecommendJson();
		return;
	}
	
	@GetMapping("test2")
	public void recommendStock2() {
		return;
	}
	
}
