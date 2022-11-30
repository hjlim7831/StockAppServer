package com.stock.recommend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StockRecommendMapper {
	
	List<String> selectUserWishlist(String user_num);
	
	List<String> selectUserStockHolding(String user_num);
	
	int selectNumberOfUsers();
}
