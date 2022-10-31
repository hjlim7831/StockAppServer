package com.user.account;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.stock.trade.StockTradeDto;

@Mapper
@Repository
public interface UserAccountMapper {
	
	// parameter로 받은 통장 정보를 DB에 insert
	void insertAccount(UserAccountDto userAccountDto);
	
	// parameter로 받은 user_num의 통장 정보 select
	UserAccountDto selectAccountByNum(String user_num);
	
	// parameter로 받은 user_num의 원화 잔고 select
	int selectBalanceByNum(String user_num);
	
	// parameter로 받은 stockTradeDto로 원화 잔고 update
	void updateBalanceByUserNum(StockTradeDto stockTradeDto);
}