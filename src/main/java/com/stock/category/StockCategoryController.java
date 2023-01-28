package com.stock.category;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("stock")
@Api(tags = {"Main Controller"}, description = "주요 증권, 주식 정보 관련 API")
public class StockCategoryController {
	
	@Autowired
	StockCategoryService stockCategoryService;

	@GetMapping("category/{category_name}")
	@ApiOperation(value = "category_name에 해당하는 종목 5개 랜덤 조회")
	@ApiImplicitParam(name = "category_name", value = "주식 종목 종류 (it[IT], ctr[건설], bio[바이오], chem[화학], ent[엔터])", required = true)
	public Map<String, Object> categoryStocks(@PathVariable String category_name){
		Map<String, Object> resultMap = new HashMap<>();
		String response = String.format("success_get_category_%s_stock", category_name);
		Object[] contents = stockCategoryService.categoryStocks(category_name);
		resultMap.put("response", response);
		resultMap.put("contents", contents);
		
		return resultMap;
	}
	
}
