package com.stock.debut;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class StockDebutController {
	
	@Autowired
	StockDebutService stockDebutService;
	
	@GetMapping("debut") // 상장 예정 주식 전체 조회
	public Map<String, Object> lookupDebutStock() {
		return stockDebutService.lookupDebutStock();
	}
}
