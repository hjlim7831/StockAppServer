package com.user.stock.wishlist;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserStockWishlistMapper {
	
	List<UserStockWishlistDto> selectWishlist(String user_num);
	
	String selectWishlistByStockCode(@Param("user_num") String user_num, @Param("stock_code") String stock_code);
	
	int insertWishlist(@Param("user_num") String user_num, @Param("stock_code") String stock_code);
	
	int deleteWishlist(@Param("user_num") String user_num, @Param("stock_code") String stock_code);
}
