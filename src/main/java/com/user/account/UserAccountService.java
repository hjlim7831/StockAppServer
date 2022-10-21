package com.user.account;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
	
	@Autowired
	UserAccountMapper userAccountMapper;

	public void makeAccount(String user_num) {
		
		// 계좌 번호 만들기
		Random rd = new Random();
		String serial_number = (rd.nextInt(900)+100)+"-"+(rd.nextInt(90)+10)+"-"+(rd.nextInt(9000)+1000);
		
		// 계좌 생성하기
		UserAccountDto userAccountDto = new UserAccountDto(serial_number, user_num, 5000000, 0, 0, 0, 0);
		userAccountMapper.insertAccount(userAccountDto);
		
		return;
	}
	
	public UserAccountDto lookupAccount(String user_num) {
		return userAccountMapper.selectAccountByNum(user_num);
	}
	
	public int lookupBalance(String user_num) {
		return userAccountMapper.selectBalanceByNum(user_num);
	}
}
