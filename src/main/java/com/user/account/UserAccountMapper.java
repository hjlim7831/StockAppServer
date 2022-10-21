package com.user.account;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserAccountMapper {
	
	void insertAccount(UserAccountDto userAccountDto);
	UserAccountDto selectAccountByNum(String user_num);
	int selectBalanceByNum(String user_num);
}