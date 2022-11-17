package com.user.stock.holding;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.stock.trade.StockTradeDto;

@Mapper
@Repository
public interface UserStockHoldingMapper {
	
	// 사용자의 모든 보유 주식을 select
	List<UserStockHoldingDto> selectAllUserStock(String user_num);
	
	// 사용자의 stock_code 종목 보유 현황을 select 
	UserStockHoldingDto selectOneUserStock(@Param("user_num") String user_num, @Param("stock_code") String stock_code);
	
	// 사용자의 stock_code 종목 now_price와 stock_cnt update
	int updateOneUserStock(StockTradeDto stockTradeDto);
	
	// 사용자의 stock_code 종목 새롭게 insert
	int insertOneUserStock(StockTradeDto stockTradeDto);
	
	// 사용자의 stock_code 종목 delete
	int deleteOneUserStock(@Param("user_num") String user_num, @Param("stock_code") String stock_code);
}