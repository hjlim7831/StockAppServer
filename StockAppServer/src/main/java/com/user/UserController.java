package com.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Resource
	private UserInfo userInfo;
	
	@Autowired
	UserService userService;
	
	@PostMapping("join") // ȸ������
	@ResponseBody
	public Map<String, Object> joinUser(@RequestParam Map<String, Object> map) {
		
		String id = map.get("id").toString();
		String password = map.get("password").toString();
		String password_confirm = map.get("password_confirm").toString();
		String simple_pwd_string = map.get("simple_pwd").toString();
		String name = map.get("name").toString();
		String nick_name = map.get("nick_name").toString();
		String email = map.get("email").toString();
		String phone_number = map.get("phone_number").toString();
		String address = map.get("address").toString();

		HashMap<String, Object> returnMap = new HashMap<>();
		
		String response = "";
		String sentence = "";
		
		// �ʼ� �Է°�(id, password, name, nick_name, email, phone_number) �� �ϳ��� ���� ���
		if (id.equals("") || password.equals("") || name.equals("") || nick_name.equals("") || email.equals("") || phone_number.equals("")) {
			response = "failure_empty_some";
			sentence = "���̵�, ��й�ȣ, �̸�, �г���, �̸���, ��ȭ��ȣ�� ��� �Է��� �ּ���.";
		}
		
		// id (1)5~20���� ������ + ���� (2)�ߺ� Ȯ�� 
		else if (!Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?!.*[^a-zA-z0-9]).{5,20}$", id)) {
			response = "failure_wrong_format_id";
			sentence = "���̵�� �����ڿ� ���ڸ� �ϳ� �̻� ������ 5 ~ 20�ڷ� �Է��� �ּ���.";
		} else if (userService.findSameId(id) != null) {
			response = "failure_duplicate_id";
			sentence = "�̹� �����ϴ� ���̵��Դϴ�.";
		}
		
		// password (1)8~15����, ������ + ���� + Ư������ (2)password�� password_confirm ����
		else if (!Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[`~!@$!%*#^?&\\(\\)\\-_=+])(?!.*[^a-zA-z0-9`~!@$!%*#^?&\\(\\)\\-_=+]).{8,15}$", password)) {
			response = "failure_wrong_format_pwd";
			sentence = "��й�ȣ�� ������, ����, Ư�����ڸ� �ϳ� �̻� ������ 8~15�ڷ� �Է��� �ּ���.";
		} else if (!password.equals(password_confirm)) {
			response = "failure_different_pwd";
			sentence = "��й�ȣ Ȯ���� Ʋ�Ƚ��ϴ�.";
		}
		
		// simple_pwd �Ϸ�  (1)6�ڸ�, ����
		else if (!Pattern.matches("^[0-9]{6}$", simple_pwd_string)) {
			response = "failure_wrong_format_simple_pwd";
			sentence = "�ɹ�ȣ�� ���� 6�ڸ��� �Է��� �ּ���.";
		}
		
		// name (1)2~10����, ����
		else if (!Pattern.matches("^[��-�R|a-z|A-Z]{2,10}$", name)) {
			response = "failure_wrong_format_name";
			sentence = "�̸��� ���� 2 ~ 10�ڷ� �Է��� �ּ���.";
		}
		
		// nickname (1)1~8����, ����, ���� (2)�ߺ� Ȯ�� 
		else if (!Pattern.matches("^[��-��|��-�R|a-z|A-Z|0-9]{1,8}$", nick_name)) {
			response = "failure_wrong_format_nick_name";
			sentence = "�г����� �ѱ�, ����, ���� 1 ~ 8�ڸ��� �Է��� �ּ���.";
		} else if (userService.findSameNickName(nick_name) != null) {
			response = "failure_duplicate_nick_name";
			sentence = "�̹� �����ϴ� �г����Դϴ�.";
		}
		
		// email (1)�̸��� ���� (2)�ߺ� Ȯ�� 
		else if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", email)) {
			response = "failure_wrong_format_email";
			sentence = "�̸��� ���Ŀ� ���� �Է��� �ּ���.";
		} else if (userService.findSameEmail(email) != null) {
			response = "failure_duplicate_email";
			sentence = "�̹� ���̵� �����ϴ� �̸����Դϴ�.";
		}
		
		// phone_number (1)10~11�ڸ�, ���� (2)�ߺ� Ȯ�� 
		else if (!Pattern.matches("^[0-9]{10,11}$", phone_number)) {
			response = "failure_wrong_format_phone_number";
			sentence = "��ȭ��ȣ�� 01022223333ó�� ���� 11�ڸ��� �Է��� �ּ���.";
		} else if (userService.findSamePhoneNumber(phone_number) != null) {
			response = "failure_duplicate_phone_number";
			sentence = "�̹� ���̵� �����ϴ� ��ȭ��ȣ�Դϴ�.";
		}
		
		// address

		else {
			// simple_pwd ���������� ����
			int simple_pwd = Integer.valueOf(simple_pwd_string);
			
			// user_num ����
			String user_num = UUID.randomUUID().toString().replace("-", "");
			
			UserDTO userDTO = new UserDTO(user_num, id, password, simple_pwd, name, nick_name, email, phone_number, address);
			userService.joinUser(userDTO);
			
			response = "success_join";
			sentence = "ȸ�������� �Ϸ�ƽ��ϴ�.";
		}

		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		return returnMap;
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
			UserDTO userDTO = userService.loginUser(id);
			
			if (userDTO == null) {
				response = "failure_notExist_id";
				sentence = "�������� �ʴ� ���̵��Դϴ�.";
			} else if (!userDTO.getPassword().equals(password)) {
				response = "failure_wrong_password";
				sentence = "�߸��� ��й�ȣ�Դϴ�.";
			} else {
				response = "success_login";
				sentence = "�α����� �Ϸ�ƽ��ϴ�.";
				
				// ���ǿ� �α��� ���� ( id, user_num ) ����
				userInfo.setId(userDTO.getId());
				userInfo.setUser_num(userDTO.getUser_num());	
			}
		}
		
		// API�� ���� �Ѱ��ֱ�
		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		return returnMap;
	}
	
	@GetMapping("session") // �α��� ����
	@ResponseBody
	public UserInfo get() {
		return userInfo;
	}
	
	@PostMapping("logout") // �α׾ƿ�
	public void logoutUser(@RequestParam Map<String, Object> map) {
		userInfo = new UserInfo();
	}
}