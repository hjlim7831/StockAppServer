package com.user.info;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.account.UserAccountService;

@Service
public class UserInfoService {
	
	@Resource // session에 저장해 둔 로그인 정보 가져오기
	private UserInfoSessionDto userInfoSessionDto;
	
	@Autowired
	UserInfoMapper userInfoMapper;
	
	@Autowired
	UserAccountService userAccountService;
	
	public Map<String, Object> joinUser(UserInfoDto userInfoDto) { // 회원가입
		
		String id = userInfoDto.getId();
		String password = userInfoDto.getPassword();
		String password_confirm = userInfoDto.getPassword_confirm();
		String simple_pwd = userInfoDto.getSimple_pwd();
		String name = userInfoDto.getName();
		String nick_name = userInfoDto.getNick_name();
		String email = userInfoDto.getEmail();
		String phone_number = userInfoDto.getPhone_number();

		HashMap<String, Object> resultMap = new HashMap<>();
		
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
			contents = "아이디는 5~20자 영문자, 숫자를 모두 포함해 입력해 주세요.";
		} else if (userInfoMapper.selectId(id) != null) {
			response = "failure_duplicate_id";
			contents = "이미 사용 중인 아이디입니다.";
		}
		
		// password (1)8~15글자, 영문자 + 숫자 + 특수문자 (2)password와 password_confirm 동일
		else if (!Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[`~!@$!%*#^?&\\(\\)\\-_=+])(?!.*[^a-zA-z0-9`~!@$!%*#^?&\\(\\)\\-_=+]).{8,15}$", password)) {
			response = "failure_wrong_format_pwd";
			contents = "비밀번호는 8~15자 영문자, 숫자, 특수문자를 모두 포함해 입력해 주세요.";
		} else if (!password.equals(password_confirm)) {
			response = "failure_different_pwd";
			contents = "입력하신 두 비밀번호가 다릅니다.";
		}
		
		// simple_pwd 완료  (1)6자리, 숫자
		else if (!Pattern.matches("^[0-9]{6}$", simple_pwd)) {
			response = "failure_wrong_format_simple_pwd";
			contents = "핀번호는 숫자 6자리로 입력해 주세요.";
		}
		
		// name (1)2~10글자, 문자
		else if (!Pattern.matches("^[가-힣|a-z|A-Z]{2,10}$", name)) {
			response = "failure_wrong_format_name";
			contents = "이름은 문자 2~10자로 입력해 주세요.";
		}
		
		// nickname (1)1~8글자, 문자, 숫자 (2)중복 확인 
		else if (!Pattern.matches("^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9]{1,8}$", nick_name)) {
			response = "failure_wrong_format_nick_name";
			contents = "닉네임은 1~8자 한글, 영어, 숫자로 입력해 주세요.";
		} else if (userInfoMapper.selectNickName(nick_name) != null) {
			response = "failure_duplicate_nick_name";
			contents = "이미 사용 중인 닉네임입니다.";
		}
		
		// email (1)이메일 형식 (2)중복 확인 
		else if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", email)) {
			response = "failure_wrong_format_email";
			contents = "이메일 형식에 맞춰 입력해 주세요.";
		} else if (userInfoMapper.selectEmail(email) != null) {
			response = "failure_duplicate_email";
			contents = "이미 사용 중인 이메일입니다.";
		}
		
		// phone_number (1)10~11자리, 숫자 (2)중복 확인 
		else if (!Pattern.matches("^[0-9]{10,11}$", phone_number)) {
			response = "failure_wrong_format_phone_number";
			contents = "전화번호는 '-' 없이 숫자 11자리로 입력해 주세요.";
		} else if (userInfoMapper.selectPhoneNumber(phone_number) != null) {
			response = "failure_duplicate_phone_number";
			contents = "이미 사용 중인 전화번호입니다.";
		}
		
		// address

		else {
			// user_num 생성 및 넣기
			String user_num = UUID.randomUUID().toString().replace("-", "");
			userInfoDto.setUser_num(user_num);
			
			// 회원 가입 성공 시, DB에 회원 정보 넣기
			userInfoMapper.insertUser(userInfoDto);
			
			// 회원가입 성공 시, 통장 개설하기
			userAccountService.makeAccount(user_num);
			
			response = "success_join";
			contents = "회원가입이 완료됐습니다.";
		}

		resultMap.put("response", response);
		resultMap.put("contents", contents);
		
		return resultMap;
	}
	
	public Map<String, Object> loginUser(UserInfoDto userInfoDto) { // 로그인
		
		String id = userInfoDto.getId();
		String password = userInfoDto.getPassword();
		
		Map<String, Object> resultMap = new HashMap<>();
		
		String response = "";
		String contents = "";
		
		// id와 password가 모두 빈 상태로 들어왔다.
		if (id.equals("") && password.equals("")) {
			response = "failure_empty_id_pwd";
			contents = "아이디와 비밀번호를 입력해 주세요.";
		}
		
		// id만 빈 상태로 들어왔다.
		else if (id.equals("")) {
			response = "failure_empty_id";
			contents = "아이디를 입력해 주세요.";
		}
		
		// password만 빈 상태로 들어왔다.
		else if (password.equals("")) {
			response = "failure_empty_password";
			contents = "비밀번호를 입력해 주세요.";
		}
		
		// id와 password 모두 입력된 상태로 들어왔다.
		else {
			// DB에서 입력한 id에 해당하는 정보 가져오기
			UserInfoDto selectUserInfoDto = userInfoMapper.selectUser(id);
			
			// id가 존재하지 않는 경우
			if (selectUserInfoDto == null) {
				response = "failure_notExist_id";
				contents = "존재하지 않는 아이디입니다.";
			}
			
			// id가 존재하고, 비밀번호는 틀린 경우
			else if (!selectUserInfoDto.getPassword().equals(password)) {
				response = "failure_wrong_password";
				contents = "잘못된 비밀번호입니다.";
			}
			
			// id가 존재하고, 비밀번호도 맞는 경우
			else {
				response = "success_login";
				contents = "로그인이 완료됐습니다.";
				
				// 로그인 정보 Session에 저장
				userInfoSessionDto.setUser_num(selectUserInfoDto.getUser_num());
				userInfoSessionDto.setId(selectUserInfoDto.getId());
			}
		}

		// API로 응답 넘겨주기
		resultMap.put("response", response);
		resultMap.put("contents", contents);
		
		return resultMap;
	}
	
	public Map<String, Object> logoutUser() { // 로그아웃
		
		// session에 있는 로그인 정보 삭제
		userInfoSessionDto.setId(null);
		userInfoSessionDto.setUser_num(null);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("response", "success_logout");
		resultMap.put("contents", "로그아웃 되었습니다.");
		
		return resultMap;
	}
	
	public Map<String, Object> loginCheck() { // 로그인 여부 확인
		Map<String, Object> resultMap = new HashMap<>();
		
		String response;
		UserInfoDto contents;

		if (userInfoSessionDto.getUser_num() == null) { // 로그인 안 돼 있는 상태
			response = "logout";
			contents = null;
		}
		
		else { // 로그인 돼 있는 상태
			response = "login";
			contents = new UserInfoDto(userInfoSessionDto.getId(), userInfoSessionDto.getUser_num());
		}

		resultMap.put("response", response);
		resultMap.put("contents", contents);
		
		return resultMap;
	}
}