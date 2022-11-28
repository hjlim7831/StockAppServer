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
	private UserAccountMapper userAccountMapper;
	
	public int makeAccount(String user_num) { // 새로 가입한 사용자의 통장 생성
		
		// 통장번호 000-00-0000으로 랜덤 생성하기
		Random rd = new Random();
		String serial_number = (rd.nextInt(900)+100)+"-"+(rd.nextInt(90)+10)+"-"+(rd.nextInt(9000)+1000);
		
		// 새로 가입한 사용자의 통장에 넣을 정보 생성하기
		UserAccountDto userAccountDto = new UserAccountDto(serial_number, user_num, 5000000, 0, 0, 0, 0);
		
		// DB에 만든 통장 정보 넣어주기
		int result = userAccountMapper.insertAccount(userAccountDto);
		
		return result;
	}
	
	public Map<String, Object> lookupAccount() { // 사용자의 통장 정보 조회
		
		// JSON으로 반환해줄 resultMap 생성
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String response = "success_lookup_account";
		UserAccountDto contents = userAccountMapper.selectAccountByNum(userInfoSessionDto.getUser_num());

		resultMap.put("contents", contents);
		resultMap.put("response", response);
		
		return resultMap;
	}
	
	public Map<String, Object> lookupBalance() { // 사용자의 원화 통장 잔고 조회
		
		// JSON으로 반환해줄 resultMap 생성
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String response = "success_lookup_account";
		Map<String, Object> contents = new HashMap<String, Object>();
		contents.put("balance", userAccountMapper.selectBalanceByNum(userInfoSessionDto.getUser_num()));
		
		resultMap.put("contents", contents);
		resultMap.put("response", response);

		return resultMap;
	}
}
