package com.user.stock.wishlist;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserStockWishlistMapper {
	
	List<UserStockWishlistDto> selectWishlist(String user_num);
	
	String selectWishlistByStockCode(String user_num, String stock_code);
	
	void insertWishlist(String user_num, String stock_code);
	
	void deleteWishlist(String user_num, String stock_code);
}
