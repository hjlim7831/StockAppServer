package com.user.info;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.account.UserAccountService;

@Service
public class UserInfoService {

	@Autowired
	UserInfoMapper userInfoMapper;
	
	@Autowired
	UserAccountService userAccountService;
	
	public Map<String, Object> joinUser(UserInfoDto userInfoDto) {
		
		String id = userInfoDto.getId();
		String password = userInfoDto.getPassword();
		String password_confirm = userInfoDto.getPassword_confirm();
		String simple_pwd = userInfoDto.getSimple_pwd();
		String name = userInfoDto.getName();
		String nick_name = userInfoDto.getNick_name();
		String email = userInfoDto.getEmail();
		String phone_number = userInfoDto.getPhone_number();

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
		} else if (findSameId(id) != null) {
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
		else if (!Pattern.matches("^[0-9]{6}$", simple_pwd)) {
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
		} else if (findSameNickName(nick_name) != null) {
			response = "failure_duplicate_nick_name";
			sentence = "�̹� �����ϴ� �г����Դϴ�.";
		}
		
		// email (1)�̸��� ���� (2)�ߺ� Ȯ�� 
		else if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", email)) {
			response = "failure_wrong_format_email";
			sentence = "�̸��� ���Ŀ� ���� �Է��� �ּ���.";
		} else if (findSameEmail(email) != null) {
			response = "failure_duplicate_email";
			sentence = "�̹� ���̵� �����ϴ� �̸����Դϴ�.";
		}
		
		// phone_number (1)10~11�ڸ�, ���� (2)�ߺ� Ȯ�� 
		else if (!Pattern.matches("^[0-9]{10,11}$", phone_number)) {
			response = "failure_wrong_format_phone_number";
			sentence = "��ȭ��ȣ�� 01011112222ó�� ���� 11�ڸ��� �Է��� �ּ���.";
		} else if (findSamePhoneNumber(phone_number) != null) {
			response = "failure_duplicate_phone_number";
			sentence = "�̹� ���̵� �����ϴ� ��ȭ��ȣ�Դϴ�.";
		}
		
		// address

		else {
			// user_num ����
			String user_num = UUID.randomUUID().toString().replace("-", "");
			userInfoDto.setUser_num(user_num);
			
			userInfoMapper.insertUser(userInfoDto);
			
			response = "success_join";
			sentence = "ȸ�������� �Ϸ�ƽ��ϴ�.";
			
			// ȸ������ ���� ��, ���� �����ϱ�
			userAccountService.makeAccount(user_num);
		}

		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		
		return returnMap;
	}
	
	public HashMap<String, Object> loginUser(UserInfoDto userInfoDto) {
		
		String id = userInfoDto.getId();
		String password = userInfoDto.getPassword();
		
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
			// DB���� �Է��� id�� �ش��ϴ� ���� ��������
			UserInfoDto selectUserInfoDto = userInfoMapper.selectUser(id);
			
			if (selectUserInfoDto == null) {
				response = "failure_notExist_id";
				sentence = "�������� �ʴ� ���̵��Դϴ�.";
			} else if (!selectUserInfoDto.getPassword().equals(password)) {
				response = "failure_wrong_password";
				sentence = "�߸��� ��й�ȣ�Դϴ�.";
			} else {
				response = "success_login";
				sentence = "�α����� �Ϸ�ƽ��ϴ�.";
			}
		}
		
		// API�� ���� �Ѱ��ֱ�
		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		
		return returnMap;
	}
	
	public String findSameId(String id) {
		return userInfoMapper.selectId(id);
	}
	
	public String findSameEmail(String email) {
		return userInfoMapper.selectEmail(email);
	}
	
	public String findSameNickName(String nick_name) {
		return userInfoMapper.selectNickName(nick_name);
	}
	
	public String findSamePhoneNumber(String phone_number) {
		return userInfoMapper.selectPhoneNumber(phone_number);
	}
}