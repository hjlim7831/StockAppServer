package com.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("error")
public class LoginInterceptorController {
	
	@GetMapping("no-login")
	public Map<String, Object> noLogin() {
		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("contents", "로그인이 돼 있지 않습니다.");
		resultMap.put("response", "failure_no_login_data");
		
		return resultMap;
	}
}
