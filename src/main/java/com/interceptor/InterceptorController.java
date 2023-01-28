package com.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("error")
@Api(tags = {"Interceptor Controller"}, description = "인터셉터 API")
public class InterceptorController {

	@RequestMapping("no-login")
	@ApiOperation(value = "로그인이 돼 있지 않은 경우")
	public Map<String, Object> noLogin() {
		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("contents", "로그인이 돼 있지 않습니다.");
		resultMap.put("response", "failure_no_login_data");

		return resultMap;
	}

	@RequestMapping("stock-close")
	@ApiOperation(value = "주식 장이 열려 있지 않은 경우")
	public Map<String, Object> stockClose() {
		Map<String,Object> resultMap = new HashMap<>();
		
		resultMap.put("contents", "장이 마감되었습니다.");
		resultMap.put("response", "failure_stock_close");		
		
		return resultMap;
	}
}
