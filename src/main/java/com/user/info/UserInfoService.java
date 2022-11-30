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
		
		Map<String, String> contents = new HashMap<>();
		int totalCheck = 0;
		
		// id 확인하기
		if (id == null || id.equals("")) {
			// 필수 입력 사항
			contents.put("id", "아이디는 필수 입력 사항입니다.");
		} else if (!Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?!.*[^a-zA-z0-9]).{5,20}$", id)) {
			// 5~20글자 영문자 + 숫자
			contents.put("id", "아이디는 5~20자 영문자, 숫자를 모두 포함해 입력해 주세요.");
		} else if (userInfoMapper.selectId(id) != null) {
			// 중복 불가능
			contents.put("id", "이미 사용 중인 아이디입니다.");
		} else {
			contents.put("id", "");
			totalCheck++;
		}
		
		// password 확인하기
		if (password == null || password.equals("")) {
			// 필수 입력 사항
			contents.put("password", "비밀번호는 필수 입력 사항입니다.");
		} else if (!Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[`~!@$!%*#^?&\\(\\)\\-_=+])(?!.*[^a-zA-z0-9`~!@$!%*#^?&\\(\\)\\-_=+]).{8,15}$", password)) {
			// 8~15글자, 영문자 + 숫자 + 특수문자
			contents.put("password", "비밀번호는 8~15자 영문자, 숫자, 특수문자를 모두 포함해 입력해 주세요.");
		} else if (!password.equals(password_confirm)) {
			// 비밀번호 확인
			contents.put("password", "입력하신 두 비밀번호가 다릅니다.");
		} else {
			contents.put("password", "");
			totalCheck++;
		}
		
		// name 확인하기
		if (name == null || name.equals("")) {
			// 필수 입력 사항
			contents.put("name", "이름은 필수 입력 사항입니다.");
		} else if (!Pattern.matches("^[가-힣|a-z|A-Z]{2,10}$", name)) {
			// 2~10글자, 문자
			contents.put("name", "이름은 문자 2~10자로 입력해 주세요.");
		} else {
			contents.put("name", "");
			totalCheck++;
		}
		
		// nick_name 확인하기
		if (nick_name == null || nick_name.equals("")) {
			// 필수 입력 사항
			contents.put("nick_name", "닉네임은 필수 입력 사항입니다.");
		} else if (!Pattern.matches("^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9]{1,8}$", nick_name)) {
			// 1~8글자, 문자, 숫자
			contents.put("nick_name", "닉네임은 1~8자 한글, 영어, 숫자로 입력해 주세요.");
		} else if (userInfoMapper.selectNickName(nick_name) != null) {
			// 중복 확인 
			contents.put("nick_name", "이미 사용 중인 닉네임입니다.");
		} else {
			contents.put("nick_name", "");
			totalCheck++;
		}
		
		// email 확인하기
		if (email == null || email.equals("")) {
			// 필수 입력 사항
			contents.put("email", "이메일은 필수 입력 사항입니다.");
		} else if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", email)) {
			// 이메일 형식
			contents.put("email", "이메일 형식에 맞춰 입력해 주세요.");
		} else if (userInfoMapper.selectEmail(email) != null) {
			// 중복 확인
			contents.put("email", "이미 사용 중인 이메일입니다.");
		} else {
			contents.put("email", "");
			totalCheck++;
		}
		
		// phone_number 확인하기
		if (phone_number == null || phone_number.equals("")) {
			// 필수 입력 사항
			contents.put("phone_number", "전화번호는 필수 입력 사항입니다.");
		} else if (!Pattern.matches("^[0-9]{10,11}$", phone_number)) {
			// 10~11자리, 숫자
			contents.put("phone_number", "전화번호는 '-' 없이 숫자 11자리로 입력해 주세요.");
		} else if (userInfoMapper.selectPhoneNumber(phone_number) != null) {
			// 중복 확인 
			contents.put("phone_number", "이미 사용 중인 전화번호입니다.");
		} else {
			contents.put("phone_number", "");
			totalCheck++;
		}

		
		// simple_pwd 확인하기
		if (!Pattern.matches("^[0-9]{6}$", simple_pwd)) {
			// 6자리, 숫자
			contents.put("simple_pwd", "핀번호는 숫자 6자리로 입력해 주세요.");
		} else {
			contents.put("simple_pwd", "");
			totalCheck++;
		}
		
		if (totalCheck < 7) {
			resultMap.put("response", "failure_wrong_format");
			resultMap.put("contents", contents);
			return resultMap;
		}
		
		// user_num 생성 및 넣기
		String user_num = UUID.randomUUID().toString().replace("-", "");
		userInfoDto.setUser_num(user_num);
		
		// DB에 회원 정보 넣기
		int result_join = userInfoMapper.insertUser(userInfoDto);
		
		if (result_join == 1) {
			// 회원가입 성공 시, 통장 개설하기
			int result_account = userAccountService.makeAccount(user_num);
			
			if (result_account == 1) {
				resultMap.put("response", "success_join");
				resultMap.put("contents", "회원가입이 완료됐습니다.");
			} else {
				resultMap.put("response", "failure_do_not_make_account");
				resultMap.put("contents", "회원가입은 완료됐으나, 통장을 생성하지 못했습니다.");
			}
		} else {
			resultMap.put("response", "failure_join");
			resultMap.put("contents", "서버상의 문제로 회원가입에 실패했습니다.");
		}

		return resultMap;
	}
	
	public Map<String, Object> loginUser(String id, String password) { // 로그인

		Map<String, Object> resultMap = new HashMap<>();

		Map<String, Object> contents = new HashMap<>();
		boolean[] loginData = new boolean[2];
		
		// id
		if (id == null || id.equals("")) {
			contents.put("id", "아이디를 입력해 주세요.");
			loginData[0] = true;
		}
		
		// password
		if (password == null || password.equals("")) {
			contents.put("password", "비밀번호를 입력해 주세요.");
			loginData[1] = true;
		}
		
		if (loginData[0] || loginData[1]) {
			if (!loginData[0]) contents.put("id", "");
			if (!loginData[1]) contents.put("password", "");
			
			resultMap.put("response", "failure_no_input_value");
			resultMap.put("contents", contents);
			
			return resultMap;
		}
		
		// DB에서 입력한 id에 해당하는 정보 가져오기
		UserInfoDto userInfoDto = userInfoMapper.selectUser(id);

		if (userInfoDto == null) {
			contents.put("id", "존재하지 않는 아이디입니다.");
			contents.put("password", "");
			
			resultMap.put("response", "failure_not_exists_id");
			resultMap.put("contents", contents);
			
			return resultMap;
		}
		
		if (!userInfoDto.getPassword().equals(password)) {
			contents.put("id", "");
			contents.put("password", "잘못된 비밀번호입니다.");
			
			resultMap.put("response", "failure_wrong_password");
			resultMap.put("contents", contents);
			
			return resultMap;
		}
		
		// 로그인 정보 Session에 저장
		userInfoSessionDto.setUser_num(userInfoDto.getUser_num());
		userInfoSessionDto.setId(userInfoDto.getId());
		userInfoSessionDto.setNick_name(userInfoDto.getNick_name());

		// API로 응답 넘겨주기
		resultMap.put("response", "success_login");
		resultMap.put("contents", "로그인이 완료됐습니다.");
		
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

		resultMap.put("response", "success_have_login_data");
		resultMap.put("contents", new UserInfoDto(userInfoSessionDto.getId(), userInfoSessionDto.getUser_num(), userInfoSessionDto.getNick_name()));
		
		return resultMap;
	}
}