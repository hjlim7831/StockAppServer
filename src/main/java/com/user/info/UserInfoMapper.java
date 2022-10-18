package com.user.info;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserInfoMapper {
	
	public void insertUser(UserInfoDto userInfoDto);
	
	public String selectId(String id);
	
	public String selectEmail(String email);
	
	public String selectNickName(String nick_name);
	
	public String selectPhoneNumber(String phone_number);
	
	public UserInfoDto selectUser(String id);
}