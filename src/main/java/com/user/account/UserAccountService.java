package com.user.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
	
	@Autowired
	UserAccountMapper userAccountMapper;
	
	public void makeAccount(String user_num) {
		
		return; 
	}
}
