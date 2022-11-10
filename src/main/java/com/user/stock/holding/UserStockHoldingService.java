package com.user.stock.holding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.info.UserInfoSessionDto;

@Service
public class UserStockHoldingService {
	
	@Resource // session에 저장해 둔 사용자 정보 가져오기
	UserInfoSessionDto userInfoSessionDto;
	
	@Autowired
	UserStockHoldingMapper userStockHoldingMapper;

	public Map<String, Object> lookupUserStockAll() { // 사용자의 모든 보유 주식 조회
		Map<String, Object> resultMap = new HashMap<>();
		
		String response = "success_lookup_user_stock";

		// session에서 가져온 user_num의 전체 보유 주식 목록을 contents에 저장
		List<UserStockHoldingDto> contents = userStockHoldingMapper.selectAllUserStock(userInfoSessionDto.getUser_num());
		
		if (contents == null || contents.size() == 0) { // 만약 List에 주식이 하나도 없다면 보유 주식이 없다.
			response = "failure_not_having_stock";
			contents = null;
		}
		
		resultMap.put("response", response);
		resultMap.put("contents", contents);
		
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
