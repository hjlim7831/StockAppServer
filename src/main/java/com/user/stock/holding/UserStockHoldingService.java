package com.user.stock.holding;

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
	
	public Map<String, Object> lookupUserStockAll() { // 사용자의 모든 보유 주식 조회
		Map<String, Object> resultMap = new HashMap<>();

		// session에서 가져온 user_num의 전체 보유 주식 목록을 contents에 저장
		List<UserStockHoldingDto> userStockHoldingDtoList = userStockHoldingMapper.selectAllUserStock(userInfoSessionDto.getUser_num());
		
		if (userStockHoldingDtoList == null || userStockHoldingDtoList.size() == 0) { // 만약 List에 주식이 하나도 없다면 보유 주식이 없다.
			resultMap.put("response", "failure_not_having_stock");
		} else {
			resultMap.put("response", "success_lookup_user_stock");
			
			for (int i=0; i<userStockHoldingDtoList.size(); i++) {
				
				UserStockHoldingDto userStockHoldingDto = userStockHoldingDtoList.get(i);
				
				String stock_code = userStockHoldingDto.getStock_code();
				int stock_cnt = userStockHoldingDto.getStock_cnt();
				
				double before_now_price = userStockHoldingDto.getNow_price();
				double now = realtimeComponent.getRealtimePrice(stock_code).getNow();
				double now_price = (now*stock_cnt) - before_now_price;
				double earnings_ratio = ((now_price - before_now_price)/before_now_price)*100;
				
				userStockHoldingDtoList.get(i).setNow_price(now_price);
				userStockHoldingDtoList.get(i).setEarnings_ratio(earnings_ratio);
			}
		}

		resultMap.put("contents", userStockHoldingDtoList);
		
		return resultMap;
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
}
