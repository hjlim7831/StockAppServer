package com.user.account;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserAccountController {
	
	@Autowired
	UserAccountService userAccountService;
	
	@GetMapping("account") // 사용자의 전체 통장 정보 조회 (로그인 여부 확인 과정 존재)
	public Map<String, Object> lookupAccount() {
		return userAccountService.lookupAccount();
	}
	
	@GetMapping("balance") // 사용자의 원화 잔고 조회 (로그인 여부 확인 과정 존재)
	public Map<String, Object> lookupBalance() {
		return userAccountService.lookupBalance();
	}
}