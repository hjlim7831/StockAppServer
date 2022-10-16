package com.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	UserMapper userMapper;
	
	public void joinUser(UserDTO userDTO) {
		userMapper.joinUser(userDTO);
	}
	
	public UserDTO loginUser(String id) {
		return userMapper.loginUser(id);
	}
	
	public String findSameId(String id) {
		return userMapper.findSameId(id);
	}
	
	public String findSameEmail(String email) {
		return userMapper.findSameEmail(email);
	}
	
	public String findSameNickName(String nick_name) {
		return userMapper.findSameNickName(nick_name);
	}
	
	public String findSamePhoneNumber(String phone_number) {
		return userMapper.findSamePhoneNumber(phone_number);
	}
}