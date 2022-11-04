package com.user.account;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.info.UserInfoSessionDto;

@Service
public class UserAccountService {
	
	@Resource // session에 저장해 둔 로그인 정보 가져오기
	private UserInfoSessionDto userInfoSessionDto;
	
	@Autowired
	UserAccountMapper userAccountMapper;

	public boolean loginCheck() { // 로그인이 됐는지 확인하는 메서드
		
		// session에 저장된 user_nun이 null이 아니라면, 로그인 상태 (true)
		if (userInfoSessionDto.getUser_num() != null) return true;
		
		// 반대로 null인 경우, 로그아웃 상태 (false)
		else return false;
	}
	
	public void makeAccount(String user_num) { // 새로 가입한 사용자의 통장 생성
		
		// 통장번호 000-00-0000으로 랜덤 생성하기
		Random rd = new Random();
		String serial_number = (rd.nextInt(900)+100)+"-"+(rd.nextInt(90)+10)+"-"+(rd.nextInt(9000)+1000);
		
		// 새로 가입한 사용자의 통장에 넣을 정보 생성하기
		UserAccountDto userAccountDto = new UserAccountDto(serial_number, user_num, 5000000, 0, 0, 0, 0);
		
		// DB에 만든 통장 정보 넣어주기
		userAccountMapper.insertAccount(userAccountDto);
		
		return;
	}
	
	public Map<String, Object> lookupAccount() { // 사용자의 통장 정보 조회
		
		// JSON으로 반환해줄 resultMap 생성
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String response;
		UserAccountDto contents;

		if (loginCheck()) { // 로그인 돼 있는 상태
			response = "success_lookup_account";
			
			// session에 저장돼 있는 user_num을 파라미터로 넘겨주고, user_num에 해당하는 통장 정보 가져오기
			contents = userAccountMapper.selectAccountByNum(userInfoSessionDto.getUser_num());
		}

		else { // 로그인 안 돼 있는 상태
			response = "failure_none_login_data";
			contents = null;
		}
		
		resultMap.put("contents", contents);
		resultMap.put("response", response);
		
		return resultMap;
	}
	
	public Map<String, Object> lookupBalance() { // 사용자의 원화 통장 잔고 조회
		
		// JSON으로 반환해줄 resultMap 생성
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String response;
		double contents;

		if (loginCheck()) { // 로그인 돼 있는 상태
			response = "success_lookup_account";
			
			// session에 저장돼 있는 user_num을 파라미터로 넘겨주고, user_num에 해당하는 통장 정보 가져오기
			contents = userAccountMapper.selectBalanceByNum(userInfoSessionDto.getUser_num());
		}

		else { // 로그인 안 돼 있는 상태
			response = "failure_none_login_data";
			contents = 0;
		}
		
		resultMap.put("contents", contents);
		resultMap.put("response", response);

		return resultMap;
	}
}
