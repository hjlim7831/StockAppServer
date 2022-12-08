package com.stock.recommend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.search.result.SearchResultDto;

@Mapper
@Repository
public interface StockRecommendMapper_SJY {
	
	List<String> selectUserWishlist(String user_num);
	
	List<String> selectUserStockHolding(String user_num);
	
	int selectNumberOfUsers();
	
	int selectUserHaveHoldingWish();
	
	List<SearchResultDto> selectStockPopular();
	
	List<String> selectAllUserNum();
	
	List<String> selectUserStock(String user_num);
	
	String[] selectAllStock();
}
