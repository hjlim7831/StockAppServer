package com.user.account;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user")
@Api(tags = {"Account Controller"}, description = "사용자 통장 관련 API")
public class UserAccountController {
	
	@Autowired
	UserAccountService userAccountService;
	
	@GetMapping("account")
	@ApiOperation(value = "사용자의 전체 통장 정보 조회")
	public Map<String, Object> lookupAccount() {
		return userAccountService.lookupAccount();
	}
	
	@GetMapping("balance")
	@ApiOperation(value = "사용자의 원화 잔고 조회")
	public Map<String, Object> lookupBalance() {
		return userAccountService.lookupBalance();
	}
}