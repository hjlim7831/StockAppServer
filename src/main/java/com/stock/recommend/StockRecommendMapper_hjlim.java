package com.stock.recommend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.search.result.SearchResultDto;

@Mapper
@Repository
public interface StockRecommendMapper_hjlim {
	
	List<String> selectUserWishlist(String user_num);
	
	List<String> selectUserStockHolding(String user_num);
	
	int selectNumberOfUsers();
	
	int selectUserHaveHoldingWish();
	
	List<SearchResultDto> selectStockPopular();
	
	List<SelectedStockDto_hjlim> selectStockCodeListHoldingWish();
	
	String selectCompanyNameByStockCode(String stock_code);
}
