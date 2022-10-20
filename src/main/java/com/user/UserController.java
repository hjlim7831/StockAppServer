package com.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.user.account.UserAccountService;
import com.user.info.UserInfoDto;
import com.user.info.UserInfoService;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	UserAccountService userAccountService;
	
	@PostMapping("join") // 회원가입
	@ResponseBody
	public Map<String, Object> joinUser(@RequestParam UserInfoDto userInfoDto) {
		Map<String, Object> resultJoin = userInfoService.joinUser(userInfoDto);
		
		if (resultJoin.get("response").toString().equals("success_join")) {
			userAccountService.makeAccount(userInfoDto.getUser_num());
		}
		
		return resultJoin;
	}
	
	@PostMapping("login") // 로그인
	@ResponseBody
	public Map<String, Object> loginUser(@RequestParam Map<String, Object> map) {
		
		String id = map.get("id").toString();
		String password = map.get("password").toString();
		
		HashMap<String, Object> returnMap = new HashMap<>();
		
		String response = "";
		String sentence = "";
		
		if (id.equals("") && password.equals("")) {
			response = "failure_empty_id_pwd";
			sentence = "아이디와 비밀번호를 입력해 주세요.";
		} else if (id.equals("")) {
			response = "failure_empty_id";
			sentence = "아이디를 입력해 주세요.";
		} else if (password.equals("")) {
			response = "failure_empty_password";
			sentence = "비밀번호를 입력해 주세요.";
		} else {
			UserInfoDto userInfoDto = userInfoService.loginUser(id);
			
			if (userInfoDto == null) {
				response = "failure_notExist_id";
				sentence = "존재하지 않는 아이디입니다.";
			} else if (!userInfoDto.getPassword().equals(password)) {
				response = "failure_wrong_password";
				sentence = "잘못된 비밀번호입니다.";
			} else {
				response = "success_login";
				sentence = "로그인이 완료됐습니다.";
				
				// 세션에 로그인 정보 ( id, user_num ) 저장
				// HttpSession session = ;
			}
		}
		
		// API로 응답 넘겨주기
		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		return returnMap;
	}
	
	@PostMapping("logout") // 로그아웃
	public void logoutUser(@RequestParam Map<String, Object> map) {
		
	}
}