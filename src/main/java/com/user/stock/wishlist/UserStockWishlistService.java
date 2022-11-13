package com.user.stock.wishlist;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimePriceDto;
import com.user.info.UserInfoSessionDto;

@Service
public class UserStockWishlistService {
	
	@Autowired
	UserStockWishlistMapper userStockWishlistMapper;
	
	@Resource // session에 저장해 둔 사용자 정보 가져오기
	UserInfoSessionDto userInfoSessionDto;
	
	@Autowired
	RealtimeComponent realtimeComponent;
	
	public Map<String, Object> lookupWishlist(String sorting_method) {
		Map<String, Object> resultMap = new HashMap<>();
		
		String user_num = userInfoSessionDto.getUser_num();
		List<UserStockWishlistDto> userStockWishlistDtoList = userStockWishlistMapper.selectWishlist(user_num);
		
		if (userStockWishlistDtoList == null || userStockWishlistDtoList.size() == 0) {
			resultMap.put("response", "failure_no_wishlist");
			resultMap.put("contents", "관심 주식이 없습니다.");
			
			return resultMap;
		} else {
			for (UserStockWishlistDto userStockWishlistDto : userStockWishlistDtoList) {
				RealtimePriceDto realtimePriceDto = realtimeComponent.getRealtimePrice(userStockWishlistDto.getStock_code());
				
				userStockWishlistDto.setDiff(realtimePriceDto.getDiff());
				userStockWishlistDto.setNow(realtimePriceDto.getNow());
				userStockWishlistDto.setRate(realtimePriceDto.getRate());
			}
			
			if (sorting_method.equals("view_cnt_desc")) Collections.sort(userStockWishlistDtoList, new ViewCntComparator().reversed());
			else if (sorting_method.equals("now_desc")) Collections.sort(userStockWishlistDtoList, new NowComparator().reversed());
			else if (sorting_method.equals("now_asc")) Collections.sort(userStockWishlistDtoList, new NowComparator());
			else if (sorting_method.equals("rate_desc")) Collections.sort(userStockWishlistDtoList, new RateComparator());
			else if (sorting_method.equals("rate_asc")) Collections.sort(userStockWishlistDtoList, new RateComparator());
			else if (sorting_method.equals("all")) {
				resultMap.put("response", "successs_lookup_wishlist_all");
				resultMap.put("contents", userStockWishlistDtoList);
				
				return resultMap;
			}
			// 해당하는 정렬 방식이 없는 경우
			else {
				resultMap.put("response", "failure_wrong_sorting_method");
				resultMap.put("contents", "잘못된 정렬 방식입니다.");
				
				return resultMap;
			}
			
			if (userStockWishlistDtoList.size() < 5) { // 관심 주식 수가 5개 미만인 경우
				resultMap.put("response", "success_lookup_less_than_5");
				resultMap.put("contents", userStockWishlistDtoList);
			} else {                                  // 관심 주식 수가 5개 이상인 경우
				resultMap.put("response", "success_lookup_only_5");
				resultMap.put("contents", userStockWishlistDtoList.subList(0, 5));
			}
			
			return resultMap;
		}

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
	
	class ViewCntComparator implements Comparator<UserStockWishlistDto> {
		@Override
		public int compare(UserStockWishlistDto ushd1, UserStockWishlistDto ushd2) {
			int view_cnt1 = ushd1.getView_cnt();
			int view_cnt2 = ushd2.getView_cnt();
			
			if (view_cnt1 > view_cnt2) return 1;
	        else if (view_cnt1 < view_cnt2) return -1;
	        return 0;
		}
	}
	
	class NowComparator implements Comparator<UserStockWishlistDto> {
		@Override
		public int compare(UserStockWishlistDto ushd1, UserStockWishlistDto ushd2) {
			double now1 = ushd1.getNow();
			double now2 = ushd2.getNow();
			
			if (now1 > now2) return 1;
	        else if (now1 < now2) return -1;
	        return 0;
		}
	}
	
	class RateComparator implements Comparator<UserStockWishlistDto> {
		@Override
		public int compare(UserStockWishlistDto ushd1, UserStockWishlistDto ushd2) {
			double rate1 = ushd1.getRate();
			double rate2 = ushd2.getRate();
			
			if (rate1 > rate2) return 1;
	        else if (rate1 < rate2) return -1;
	        return 0;
		}
	}
}
