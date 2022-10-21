package com.user.info;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.user.account.UserAccountService;

@Controller
@RequestMapping("user")
@SessionAttributes("loginUser")
public class UserInfoController {
	
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
	public Map<String, Object> loginUser(UserInfoDto userInfoDto, Model model) {
		Map<String, Object> resultMap = userInfoService.loginUser(userInfoDto);
		
		if (resultMap.get("response").toString().equals("success_join")) {
			userInfoDto.setUser_num((String) resultMap.get("user_num"));
			resultMap.remove("user_num");
			
			model.addAttribute("loginUser", userInfoDto);
		}
		
		return resultMap;
	}
	
	@PostMapping("logout") // 로그아웃
	@ResponseBody
	public Map<String, Object> logoutUser(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("contents", "로그아웃 되었습니다.");
		resultMap.put("response", "success_logout");
		
		return resultMap;
	}
}