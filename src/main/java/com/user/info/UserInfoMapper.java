package com.user.info;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserInfoMapper {
	
	// parameter로 받은 사용자 정보를 넘겨 받아 insert
	public void insertUser(UserInfoDto userInfoDto);
	
	// parameter로 받은 id에 해당하는 id 정보를 select
	public String selectId(String id);
	
	// parameter로 받은 email에 해당하는 id 정보를 select
	public String selectEmail(String email);
	
	// parameter로 받은 nick_name에 해당하는 id 정보를 select
	public String selectNickName(String nick_name);
	
	// parameter로 받은 phone_number에 해당하는 id 정보를 select
	public String selectPhoneNumber(String phone_number);
	
	// parameter로 받은 id에 해당하는 사용자의 id, password, user_num 정보를 select
	public UserInfoDto selectUser(String id);
}