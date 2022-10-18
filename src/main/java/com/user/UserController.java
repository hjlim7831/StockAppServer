package com.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.user.account.UserAccountService;
import com.user.info.UserInfoDto;
import com.user.info.UserInfoService;

@Controller
@RequestMapping("user")
@SessionAttributes("loginUser")
public class UserController {
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	UserAccountService userAccountService;
	
	@PostMapping("join") // 회원가입
	@ResponseBody
	public Map<String, Object> joinUser(UserInfoDto userInfoDto) {
		Map<String, Object> resultMap = userInfoService.joinUser(userInfoDto);

		return resultMap;
	}
	
	@PostMapping("login") // 로그인
	@ResponseBody
	public Map<String, Object> loginUser(UserInfoDto userInfoDto) {
		Map<String, Object> resultMap = userInfoService.loginUser(userInfoDto);
		
		// 로그인 성공 시, 로그인 정보 세션에 넣어주기
		if (resultMap.get("response").toString().equals("success_login")) {
			setSession(userInfoDto);
		}
		
		return resultMap;
	}
	
	@ModelAttribute("loginUser") // session에 로그인 데이터 저장
	public UserInfoDto setSession(UserInfoDto userInfoDto) {
		return userInfoDto;
	}
	
	@PostMapping("logout") // 로그아웃
	@ResponseBody
	public Map<String, Object> logoutUser(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("response", "success_logout");
		resultMap.put("contents", "로그아웃 되었습니다.");
		
		return resultMap;
	}
}