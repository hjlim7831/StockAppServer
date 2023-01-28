package com.user.info;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.account.UserAccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user")
@Api(tags = {"User Controller"}, description = "사용자 정보 관련 API")
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
	
	@DeleteMapping("sign-out")
	@ApiOperation(value = "회원탈퇴")
	public Map<String, Object> signOutUser(){
		return userInfoService.signOutUser();
	}
}