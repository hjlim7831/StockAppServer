package com.stock.recommend;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.search.result.SearchResultDto;

@Mapper
@Repository
public interface StockRecommendMapper_SJY {
	
	int selectAllWishAndHolding();
	
	int selectNumberOfUsers();
	
	int selectUserHaveHoldingWish();
	
	List<SearchResultDto> selectStockPopular();
	
	// DB에 존재하는 모든 사용자 고유 번호를 가져온다.
	String[] selectAllUserNum();
	
	// user_num 사용자의 보유 또는 관심 주식에 있는 모든 주식 코드를 가져온다.
	Set<String> selectUserStock(String user_num);
	
	// DB에 있는 모든 주식 코드를 가져온다.
	String[] selectAllStock();
	
	String selectCompanyName(String stock_code);
}
