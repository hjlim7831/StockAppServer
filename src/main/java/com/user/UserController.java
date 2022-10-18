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
	
	@PostMapping("join") // ȸ������
	@ResponseBody
	public Map<String, Object> joinUser(UserInfoDto userInfoDto) {
		Map<String, Object> resultMap = userInfoService.joinUser(userInfoDto);

		return resultMap;
	}
	
	@PostMapping("login") // �α���
	@ResponseBody
	public Map<String, Object> loginUser(UserInfoDto userInfoDto) {
		Map<String, Object> resultMap = userInfoService.loginUser(userInfoDto);
		
		// �α��� ���� ��, �α��� ���� ���ǿ� �־��ֱ�
		if (resultMap.get("response").toString().equals("success_login")) {
			setSession(userInfoDto);
		}
		
		return resultMap;
	}
	
	@ModelAttribute("loginUser") // session�� �α��� ������ ����
	public UserInfoDto setSession(UserInfoDto userInfoDto) {
		return userInfoDto;
	}
	
	@PostMapping("logout") // �α׾ƿ�
	@ResponseBody
	public Map<String, Object> logoutUser(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("response", "success_logout");
		resultMap.put("contents", "�α׾ƿ� �Ǿ����ϴ�.");
		
		return resultMap;
	}
}