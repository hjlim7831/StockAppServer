package com.user.stock.holding;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.stock.trade.StockTradeDto;

@Mapper
@Repository
public interface UserStockHoldingMapper {
	
	// 사용자의 모든 보유 주식을 select
	List<UserStockHoldingDto> selectAllUserStock(String user_num);
	
	// 사용자의 stock_code 종목 보유 현황을 select 
	UserStockHoldingDto selectOneUserStock(String user_num, String stock_code);
	
	// 사용자의 stock_code 종목 now_price와 stock_cnt update
	void updateOneUserStock(StockTradeDto stockTradeDto);
	
	// 사용자의 stock_code 종목 새롭게 insert
	void insertOneUserStock(StockTradeDto stockTradeDto);
	
	// 사용자의 stock_code 종목 delete
	void deleteOneUserStock(String user_num, String stock_code);
}