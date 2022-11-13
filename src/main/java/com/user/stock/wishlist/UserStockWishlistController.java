package com.user.stock.wishlist;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/stock")
public class UserStockWishlistController {
	
	@Autowired
	UserStockWishlistService userStockWishlistService;
	
	@GetMapping("wishlist/{sorting_method}")
	public Map<String, Object> lookupWishlist(@PathVariable String sorting_method) {
		return userStockWishlistService.lookupWishlist(sorting_method);
	}
	
	@PostMapping("wishlist/{stock_code}/0")
	public Map<String, Object> addWishlist(@PathVariable String stock_code) {
		return userStockWishlistService.addWishlist(stock_code);
	}
	
	@PostMapping("wishlist/{stock_code}/1")
	public Map<String, Object> removeWishlist(@PathVariable String stock_code) {
		return userStockWishlistService.removeWishlist(stock_code);
	}
	
	@GetMapping("wishlist/{stock_code}/check")
	public Map<String, Object> checkWishlist(@PathVariable String stock_code) {
		return userStockWishlistService.checkWishlist(stock_code);
	}
}