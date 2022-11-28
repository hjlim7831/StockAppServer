package com.user.stock.holding;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.user.info.UserInfoSessionDto;

@Service
public class UserStockHoldingService {
	
	@Resource // session에 저장해 둔 사용자 정보 가져오기
	UserInfoSessionDto userInfoSessionDto;
	
	@Autowired
	UserStockHoldingMapper userStockHoldingMapper;
	
	@Autowired
	RealtimeComponent realtimeComponent;
	
	public Map<String, Object> lookupUserStock(String sorting_method) { // sorting_method에 따른 사용자의 보유 주식 조회
		Map<String, Object> resultMap = new HashMap<>();

		// session에서 가져온 user_num의 전체 보유 주식 목록을 가져온다.
		List<UserStockHoldingDto> userStockHoldingDtoList = userStockHoldingMapper.selectAllUserStock(userInfoSessionDto.getUser_num());
		
		// 만약 보유 주식이 없다면
		if (userStockHoldingDtoList == null || userStockHoldingDtoList.size() == 0) {
			resultMap.put("response", "failure_not_having_stock");
			resultMap.put("contents", "보유 주식이 없습니다.");
			
			return resultMap;
		}
		
		// 보유 주식이 1개 이상 존재한다면
		else {
			
			// user_num의 보유 주식 list를 반복하면서 현재가(now_price), 수익률(earnings_ratio)을 계산해 넣어준다.
			for (int i=0; i<userStockHoldingDtoList.size(); i++) {
				
				UserStockHoldingDto userStockHoldingDto = userStockHoldingDtoList.get(i);
				
				String stock_code = userStockHoldingDto.getStock_code();
				int stock_cnt = userStockHoldingDto.getStock_cnt();
				
				double before_now_price = userStockHoldingDto.getNow_price();
				double now = realtimeComponent.getRealtimePrice(stock_code).getNow();
				double now_price = now*stock_cnt;
				double earnings_ratio = ((now_price - before_now_price)/before_now_price)*100;
				
				userStockHoldingDtoList.get(i).setNow_price(now_price);
				userStockHoldingDtoList.get(i).setEarnings_ratio(earnings_ratio);
			}
			
			// 보유 개수 기준으로 내림차순 정렬
			if (sorting_method.equals("cnt_desc")) Collections.sort(userStockHoldingDtoList, new CntComparator().reversed());
			
			// 수익률 기준으로 내림차순 정렬
			else if (sorting_method.equals("ratio_desc")) Collections.sort(userStockHoldingDtoList, new RatioComparator().reversed());
			
			// 수익률 기준으로 오름차순 정렬
			else if (sorting_method.equals("ratio_asc")) Collections.sort(userStockHoldingDtoList, new RatioComparator());
			
			// 현재 가치 기준으로 오름차순 정렬
			else if (sorting_method.equals("price_desc")) Collections.sort(userStockHoldingDtoList, new PriceComparator().reversed());
			
			// 정렬 없이 전체를 조회하는 경우
			else if (sorting_method.equals("all")) {
				resultMap.put("response", "success_lookup_user_stock_all");
				resultMap.put("contents", userStockHoldingDtoList);
				
				return resultMap;
			}
			
			// 해당하는 정렬 방식이 없는 경우
			else {
				resultMap.put("response", "failure_wrong_sorting_method");
				resultMap.put("contents", "잘못된 정렬 방식입니다.");
				
				return resultMap;
			}
			
			if (userStockHoldingDtoList.size() < 5) { // 보유 주식 수가 5개 미만인 경우
				resultMap.put("response", "success_lookup_less_than_5");
				resultMap.put("contents", userStockHoldingDtoList);
			} else {                                  // 보유 주식 수가 5개 이상인 경우
				resultMap.put("response", "success_lookup_only_5");
				resultMap.put("contents", userStockHoldingDtoList.subList(0, 5));
			}
			
			return resultMap;
		}
	}
	
	public Map<String, Object> lookupUserStockOne(String stock_code) { // 사용자의 stock_code 보유 현황 조회
		Map<String, Object> resultMap = new HashMap<>();
		
		String response = "success_lookup_user_stock";
		int contents = 0;
		
		// DB에서 user_num의 stock_code 보유량 가져오기
		UserStockHoldingDto userStockHoldingDto = userStockHoldingMapper.selectOneUserStock(userInfoSessionDto.getUser_num(), stock_code);
		
		if (userStockHoldingDto == null) response = "success_get_holding_this_stock";  // 보유량이 없는 경우
		else contents = userStockHoldingDto.getStock_cnt();   // user_num의 stock_code 보유량을 contents에 저장

		resultMap.put("response", response);
		resultMap.put("contents", contents);
		
		return resultMap;
	}
	
	class CntComparator implements Comparator<UserStockHoldingDto> {
		@Override
		public int compare(UserStockHoldingDto ushd1, UserStockHoldingDto ushd2) {
			int stock_cnt1 = ushd1.getStock_cnt();
			int stock_cnt2 = ushd2.getStock_cnt();
			
			if (stock_cnt1 > stock_cnt2) return 1;
	        else if (stock_cnt1 < stock_cnt2) return -1;
	        return 0;
		}
	}
	
	class RatioComparator implements Comparator<UserStockHoldingDto> {
		@Override
		public int compare(UserStockHoldingDto ushd1, UserStockHoldingDto ushd2) {
			double earnings_ratio1 = ushd1.getEarnings_ratio();
			double earnings_ratio2 = ushd2.getEarnings_ratio();
			
			if (earnings_ratio1 > earnings_ratio2) return 1;
	        else if (earnings_ratio1 < earnings_ratio2) return -1;
	        return 0;
		}
	}
	
	class PriceComparator implements Comparator<UserStockHoldingDto> {
		@Override
		public int compare(UserStockHoldingDto ushd1, UserStockHoldingDto ushd2) {
			double now_price1 = ushd1.getNow_price();
			double now_price2 = ushd2.getNow_price();
			
			if (now_price1 > now_price2) return 1;
	        else if (now_price1 < now_price2) return -1;
	        return 0;
		}
	}
}
