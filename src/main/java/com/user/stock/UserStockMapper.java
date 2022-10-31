package com.user.stock;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserStockMapper {
	
	// 사용자의 모든 보유 주식을 select
	List<UserStockDto> selectAllUserStock(String user_num);
	
	// 사용자의 stock_code 종목 보유 현황을 select 
	UserStockDto selectOneUserStock(UserStockDto userStockDto);
}