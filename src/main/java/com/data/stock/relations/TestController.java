package com.data.stock.relations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@Autowired
	CalculateStockRelations calculateStockRelations;
	
	@GetMapping("test")
	public void test() {
		calculateStockRelations.findStockRelations();
	}
}
