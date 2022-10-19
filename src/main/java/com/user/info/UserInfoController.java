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

	@PostMapping("join") // ȸ������
	@ResponseBody
	public Map<String, Object> joinUser(UserInfoDto userInfoDto) {
		Map<String, Object> resultMap = userInfoService.joinUser(userInfoDto);
		return resultMap;
	}
	
	@PostMapping("login") // �α���
	@ResponseBody
	public Map<String, Object> loginUser(UserInfoDto userInfoDto, Model model) {
		Map<String, Object> resultMap = userInfoService.loginUser(userInfoDto);
		
		if (resultMap.get("response").toString().equals("success_join")) {
			model.addAttribute("loginUser", userInfoDto);
		}
		
		return resultMap;
	}
	
	@PostMapping("logout") // �α׾ƿ�
	@ResponseBody
	public Map<String, Object> logoutUser(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("contents", "�α׾ƿ� �Ǿ����ϴ�.");
		resultMap.put("response", "success_logout");
		
		return resultMap;
	}
}