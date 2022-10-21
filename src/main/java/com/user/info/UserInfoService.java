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
		String contents = "";
		
		// 필수 입력값(id, password, name, nick_name, email, phone_number) 중 하나라도 없는 경우
		if (id.equals("") || password.equals("") || name.equals("") || nick_name.equals("") || email.equals("") || phone_number.equals("")) {
			response = "failure_empty_some";
			contents = "아이디, 비밀번호, 이름, 닉네임, 이메일, 전화번호를 모두 입력해 주세요.";
		}
		
		// id (1)5~20글자 영문자 + 숫자 (2)중복 확인 
		else if (!Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?!.*[^a-zA-z0-9]).{5,20}$", id)) {
			response = "failure_wrong_format_id";
			contents = "아이디는 영문자와 숫자를 하나 이상 포함해 5 ~ 20자로 입력해 주세요.";
		} else if (findSameId(id) != null) {
			response = "failure_duplicate_id";
			contents = "이미 존재하는 아이디입니다.";
		}
		
		// password (1)8~15글자, 영문자 + 숫자 + 특수문자 (2)password와 password_confirm 동일
		else if (!Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[`~!@$!%*#^?&\\(\\)\\-_=+])(?!.*[^a-zA-z0-9`~!@$!%*#^?&\\(\\)\\-_=+]).{8,15}$", password)) {
			response = "failure_wrong_format_pwd";
			contents = "비밀번호는 영문자, 숫자, 특수문자를 하나 이상 포함해 8~15자로 입력해 주세요.";
		} else if (!password.equals(password_confirm)) {
			response = "failure_different_pwd";
			contents = "비밀번호 확인이 틀렸습니다.";
		}
		
		// simple_pwd 완료  (1)6자리, 숫자
		else if (!Pattern.matches("^[0-9]{6}$", simple_pwd)) {
			response = "failure_wrong_format_simple_pwd";
			contents = "핀번호는 숫자 6자리로 입력해 주세요.";
		}
		
		// name (1)2~10글자, 문자
		else if (!Pattern.matches("^[가-힣|a-z|A-Z]{2,10}$", name)) {
			response = "failure_wrong_format_name";
			contents = "이름은 문자 2 ~ 10자로 입력해 주세요.";
		}
		
		// nickname (1)1~8글자, 문자, 숫자 (2)중복 확인 
		else if (!Pattern.matches("^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9]{1,8}$", nick_name)) {
			response = "failure_wrong_format_nick_name";
			contents = "닉네임은 한글, 영어, 숫자 1 ~ 8자리로 입력해 주세요.";
		} else if (findSameNickName(nick_name) != null) {
			response = "failure_duplicate_nick_name";
			contents = "이미 존재하는 닉네임입니다.";
		}
		
		// email (1)이메일 형식 (2)중복 확인 
		else if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", email)) {
			response = "failure_wrong_format_email";
			contents = "이메일 형식에 맞춰 입력해 주세요.";
		} else if (findSameEmail(email) != null) {
			response = "failure_duplicate_email";
			contents = "이미 아이디가 존재하는 이메일입니다.";
		}
		
		// phone_number (1)10~11자리, 숫자 (2)중복 확인 
		else if (!Pattern.matches("^[0-9]{10,11}$", phone_number)) {
			response = "failure_wrong_format_phone_number";
			contents = "전화번호는 01011112222처럼 숫자 11자리로 입력해 주세요.";
		} else if (findSamePhoneNumber(phone_number) != null) {
			response = "failure_duplicate_phone_number";
			contents = "이미 아이디가 존재하는 전화번호입니다.";
		}
		
		// address

		else {
			// user_num 생성
			String user_num = UUID.randomUUID().toString().replace("-", "");
			userInfoDto.setUser_num(user_num);
			
			// 회원 가입 성공 시, DB에 회원 정보 넣기
			userInfoMapper.insertUser(userInfoDto);
			
			// 회원가입 성공 시, 통장 개설하기
			userAccountService.makeAccount(user_num);
			
			response = "success_join";
			contents = "회원가입이 완료됐습니다.";
		}

		returnMap.put("contents", contents);
		returnMap.put("response", response);
		
		return returnMap;
	}
	
	public HashMap<String, Object> loginUser(UserInfoDto userInfoDto) {
		
		String id = userInfoDto.getId();
		String password = userInfoDto.getPassword();
		
		HashMap<String, Object> returnMap = new HashMap<>();
		
		String response = "";
		String contents = "";
		
		if (id.equals("") && password.equals("")) {
			response = "failure_empty_id_pwd";
			contents = "아이디와 비밀번호를 입력해 주세요.";
		} else if (id.equals("")) {
			response = "failure_empty_id";
			contents = "아이디를 입력해 주세요.";
		} else if (password.equals("")) {
			response = "failure_empty_password";
			contents = "비밀번호를 입력해 주세요.";
		} else {
			// DB에서 입력한 id에 해당하는 정보 가져오기
			UserInfoDto selectUserInfoDto = userInfoMapper.selectUser(id);
			
			if (selectUserInfoDto == null) {
				response = "failure_notExist_id";
				contents = "존재하지 않는 아이디입니다.";
			} else if (!selectUserInfoDto.getPassword().equals(password)) {
				response = "failure_wrong_password";
				contents = "잘못된 비밀번호입니다.";
			} else {
				response = "success_login";
				contents = "로그인이 완료됐습니다.";
			}
		}
		
		// API로 응답 넘겨주기
		returnMap.put("contents", contents);
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