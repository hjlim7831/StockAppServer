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
	
	@PostMapping("join") // ȸ������
	@ResponseBody
	public Map<String, Object> joinUser(@RequestParam UserInfoDto userInfoDto) {
		Map<String, Object> resultJoin = userInfoService.joinUser(userInfoDto);
		
		if (resultJoin.get("response").toString().equals("success_join")) {
			userAccountService.makeAccount(userInfoDto.getUser_num());
		}
		
		return resultJoin;
	}
	
	@PostMapping("login") // �α���
	@ResponseBody
	public Map<String, Object> loginUser(@RequestParam Map<String, Object> map) {
		
		String id = map.get("id").toString();
		String password = map.get("password").toString();
		
		HashMap<String, Object> returnMap = new HashMap<>();
		
		String response = "";
		String sentence = "";
		
		if (id.equals("") && password.equals("")) {
			response = "failure_empty_id_pwd";
			sentence = "���̵�� ��й�ȣ�� �Է��� �ּ���.";
		} else if (id.equals("")) {
			response = "failure_empty_id";
			sentence = "���̵� �Է��� �ּ���.";
		} else if (password.equals("")) {
			response = "failure_empty_password";
			sentence = "��й�ȣ�� �Է��� �ּ���.";
		} else {
			UserInfoDto userInfoDto = userInfoService.loginUser(id);
			
			if (userInfoDto == null) {
				response = "failure_notExist_id";
				sentence = "�������� �ʴ� ���̵��Դϴ�.";
			} else if (!userInfoDto.getPassword().equals(password)) {
				response = "failure_wrong_password";
				sentence = "�߸��� ��й�ȣ�Դϴ�.";
			} else {
				response = "success_login";
				sentence = "�α����� �Ϸ�ƽ��ϴ�.";
				
				// ���ǿ� �α��� ���� ( id, user_num ) ����
				// HttpSession session = ;
			}
		}
		
		// API�� ���� �Ѱ��ֱ�
		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		return returnMap;
	}
	
	@PostMapping("logout") // �α׾ƿ�
	public void logoutUser(@RequestParam Map<String, Object> map) {
		
	}
}