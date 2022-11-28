package com.user.info;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.user.account.UserAccountService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user")
@SessionAttributes("loginUser")
public class UserInfoController {

	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	UserAccountService userAccountService;
	
	@PostMapping("join")
	@ApiOperation(value = "회원가입")
	public Map<String, Object> joinUser(UserInfoDto userInfoDto) {
		return userInfoService.joinUser(userInfoDto);
	}

	@PostMapping("login")
	@ApiOperation(value = "로그인")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "아이디", required = true),
		@ApiImplicitParam(name = "password", value = "비밀번호", required = true)
	})
	public Map<String, Object> loginUser(String id, String password) {
		return userInfoService.loginUser(id, password);
	}
	
	@PostMapping("logout")
	@ApiOperation(value = "로그아웃")
	public Map<String, Object> logoutUser() {
		return userInfoService.logoutUser();
	}
	
	@GetMapping("login-session")
	@ApiOperation(value = "로그인 정보 확인")
	public Map<String, Object> loginCheck() {
		return userInfoService.loginCheck();
	}
	
	// 중복 확인 id, nickname, email, phone-number
}