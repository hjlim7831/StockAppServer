package com.user.stock.wishlist;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("user/stock")
public class UserStockWishlistController {
	
	@Autowired
	UserStockWishlistService userStockWishlistService;
	
	@ApiOperation(value = "사용자의 wishlist를 정렬 방식에 따라 조회")
	@GetMapping("wishlist/{sorting_method}")
	public Map<String, Object> lookupWishlist(@PathVariable String sorting_method) {
		return userStockWishlistService.lookupWishlist(sorting_method);
	}
	
	@ApiOperation(value = "사용자의 wishlist에 해당 주식 종목 추가")
	@PostMapping("wishlist/{stock_code}")
	public Map<String, Object> addWishlist(@PathVariable String stock_code) {
		return userStockWishlistService.addWishlist(stock_code);
	}
	
	@ApiOperation(value = "사용자의 wishlist에 해당 주식 종목 삭제")
	@DeleteMapping("wishlist/{stock_code}")
	public Map<String, Object> removeWishlist(@PathVariable String stock_code) {
		return userStockWishlistService.removeWishlist(stock_code);
	}
	
	@ApiOperation(value = "사용자의 wishlist에 해당 주식 종목이 있는지 확인")
	@GetMapping("wishlist/{stock_code}")
	public Map<String, Object> checkWishlist(@PathVariable String stock_code) {
		return userStockWishlistService.checkWishlist(stock_code);
	}
}