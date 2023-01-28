package com.user.stock.wishlist;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user/stock")
@Api(tags = {"Wishlist Controller"}, description = "사용자 관심 주식 관련 API")
public class UserStockWishlistController {
	
	@Autowired
	UserStockWishlistService userStockWishlistService;
	
	@GetMapping("wishlist")
	@ApiOperation(value = "사용자의 wishlist를 정렬 방식에 따라 조회")
	@ApiImplicitParam(name = "sorting_method", value = "정렬 방식 (all, view_cnt_desc, rate_desc, rate_asc, now_desc, now_asc)", required = true)
	public Map<String, Object> lookupWishlist(String sorting_method) {
		return userStockWishlistService.lookupWishlist(sorting_method);
	}
	
	@PostMapping("wishlist/{stock_code}")
	@ApiOperation(value = "사용자의 wishlist에 해당 주식 종목 추가")
	@ApiImplicitParam(name = "stock_code", value = "주식 종목 코드 (숫자 6자리)", required = true)
	public Map<String, Object> addWishlist(@PathVariable String stock_code) {
		return userStockWishlistService.addWishlist(stock_code);
	}
	
	@DeleteMapping("wishlist/{stock_code}")
	@ApiOperation(value = "사용자의 wishlist에 해당 주식 종목 삭제")
	@ApiImplicitParam(name = "stock_code", value = "주식 종목 코드 (숫자 6자리)", required = true)
	public Map<String, Object> removeWishlist(@PathVariable String stock_code) {
		return userStockWishlistService.removeWishlist(stock_code);
	}
	
	@ApiOperation(value = "사용자의 wishlist에 해당 주식 종목이 있는지 확인")
	@GetMapping("wishlist/{stock_code}")
	public Map<String, Object> checkWishlist(@PathVariable String stock_code) {
		return userStockWishlistService.checkWishlist(stock_code);
	}
}