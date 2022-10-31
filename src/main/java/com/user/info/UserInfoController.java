package com.user.info;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.user.account.UserAccountService;

@RestController
@RequestMapping("user")
@SessionAttributes("loginUser")
public class UserInfoController {

	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	UserAccountService userAccountService;
	
	@PostMapping("join") // 회원가입
	public Map<String, Object> joinUser(UserInfoDto userInfoDto) {
		return userInfoService.joinUser(userInfoDto);
	}
	
	@PostMapping("login") // 로그인 (로그아웃이 돼 있다고 가정)
	public Map<String, Object> loginUser(UserInfoDto userInfoDto, Model model) {
		return userInfoService.loginUser(userInfoDto);
	}
	
	@PostMapping("logout") // 로그아웃 (로그인이 돼 있다고 가정)
	public Map<String, Object> logoutUser() {
		return userInfoService.logoutUser();
	}
	
	@GetMapping("login-session") // 로그인 상태 및 정보 확인
	public Map<String, Object> loginCheck() {
		return userInfoService.loginCheck();
	}
}