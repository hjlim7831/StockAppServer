package com.stock.category;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class StockCategoryController {
	
	@Autowired
	StockCategoryService stockCategoryService;

	@GetMapping("category/{category_name}")
	public Map<String, Object> categoryStocks(@PathVariable String category_name){
		
		stockCategoryService.categoryStocks(category_name);
		
		
		return null;
	}
	
}
