package com.user.stock.wishlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.user.info.UserInfoSessionDto;

@Service
public class UserStockWishlistService {
	
	@Autowired
	UserStockWishlistMapper userStockWishlistMapper;
	
	@Resource // session에 저장해 둔 사용자 정보 가져오기
	UserInfoSessionDto userInfoSessionDto;
	
	@Autowired
	RealtimeComponent realtimeComponent;
	
	public Map<String, Object> lookupWishlist() {
		Map<String, Object> resultMap = new HashMap<>();
		
		String user_num = userInfoSessionDto.getUser_num();
		List<String> wishlist = userStockWishlistMapper.selectWishlist(user_num);
		
		if (wishlist == null || wishlist.size() == 0) {
			resultMap.put("response", "failure_no_wishlist");
			resultMap.put("contents", "관심 주식이 없습니다.");
		} else {
			List<Map<String, Object>> contents = new ArrayList<>();
			for (String code : wishlist) {
				Map<String, Object> wish = new HashMap<>();
				
				wish.put("stock_code", code);
				wish.put("realtime", realtimeComponent.getRealtimePrice(code));
				
				contents.add(wish);
			}
			resultMap.put("response", "successs_lookup_wishlist");
			resultMap.put("contents", contents);
		}

		return resultMap;
	}
	
	public Map<String, Object> addWishlist(String stock_code) {
		String user_num = userInfoSessionDto.getUser_num();
		userStockWishlistMapper.insertWishlist(user_num, stock_code);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("response", "success_add_wishlist");
		resultMap.put("contents", "관심 종목에 추가됐습니다.");
		
		return resultMap;
	}
	
	public Map<String, Object> removeWishlist(String stock_code) {
		String user_num = userInfoSessionDto.getUser_num();
		userStockWishlistMapper.deleteWishlist(user_num, stock_code);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("response", "success_delete_wishlist");
		resultMap.put("contents", "관심 종목에서 제거됐습니다.");
		
		return resultMap;
	}

	public Map<String, Object> checkWishlist(String stock_code) {
		Map<String, Object> resultMap = new HashMap<>();
		
		String user_num = userInfoSessionDto.getUser_num();
		String rlt = userStockWishlistMapper.selectWishlistByStockCode(user_num, stock_code);
		
		if (rlt == null || rlt == "") {
			resultMap.put("response", "success_no_wishlist");
			resultMap.put("contents", false);
		} else {
			resultMap.put("response", "success_yes_wishlist");
			resultMap.put("contents", true);
		}
		return resultMap;
	}
}
