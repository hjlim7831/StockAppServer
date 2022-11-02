package com.user.stock.holding;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserStockHoldingMapper {
	
	// 사용자의 모든 보유 주식을 select
	List<UserStockHoldingDto> selectAllUserStock(String user_num);
	
	// 사용자의 stock_code 종목 보유 현황을 select 
	UserStockHoldingDto selectOneUserStock(UserStockHoldingDto userStockDto);
}