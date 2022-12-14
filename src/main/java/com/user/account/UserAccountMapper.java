package com.user.account;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserAccountMapper {
	
	// parameter로 받은 통장 정보를 DB에 insert
	int insertAccount(UserAccountDto userAccountDto);
	
	// parameter로 받은 user_num의 통장 정보 select
	UserAccountDto selectAccountByNum(String user_num);
	
	// parameter로 받은 user_num의 원화 잔고 select
	double selectBalanceByNum(String user_num);
	
	// parameter로 받은 값들로 원화 잔고 update
	int updateBalanceByUserNum(@Param("balance") double balance, @Param("user_num") String user_num);
	
	// parameter로 받은 값들로 외화 잔고 update
	int updateCurrencyByUserNum(@Param("user_num") String user_num, @Param("country_name") String country_name, @Param("total") double total);
	
	// 외화 잔고 가져오기
	double selectOneCurrency(@Param("user_num") String user_num, @Param("country_name") String country_name);
}