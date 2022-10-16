package com.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
	
	public void joinUser(UserDTO userDTO);
	
	public UserDTO loginUser(String id);
	
	public String findSameId(String id);
	
	public String findSameEmail(String email);
	
	public String findSameNickName(String nick_name);
	
	public String findSamePhoneNumber(String phone_number);
}