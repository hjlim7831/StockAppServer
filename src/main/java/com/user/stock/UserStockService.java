package com.user.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.info.UserInfoSessionDto;

@Service
public class UserStockService {
	
	@Resource // session에 저장해 둔 사용자 정보 가져오기
	UserInfoSessionDto userInfoSessionDto;
	
	@Autowired
	UserStockMapper userStockMapper;

	public Map<String, Object> lookupUserStockAll() { // 사용자의 모든 보유 주식 조회
		Map<String, Object> resultMap = new HashMap<>();
		
		String response = "success_lookup_user_stock";
		
		// session에 저장해둔 user_num을 가져온다.
		String user_num = userInfoSessionDto.getUser_num();
		
		// session에서 가져온 user_num의 전체 보유 주식 목록을 contents에 저장
		List<UserStockDto> contents = userStockMapper.selectAllUserStock(user_num);
		
		if (contents.size() == 0) { // 만약 List에 주식이 하나도 없다면 보유 주식이 없다.
			response = "failure_not_having_stock";
			contents = null;
		}
		
		resultMap.put("response", response);
		resultMap.put("contents", contents);
		
		return resultMap;
	}
	
	public Map<String, Object> lookupUserStockOne(String stock_code) { // 사용자의 stock_code 보유 현황 조회
		Map<String, Object> resultMap = new HashMap<>();
		
		// 전달 받은 stock_code와 session에 저장된 user_num으로 UserStockDto 생성
		UserStockDto userStockDto = new UserStockDto(stock_code, userInfoSessionDto.getUser_num());
		
		String response = "success_lookup_user_stock";
		
		// user_num의 stock_code 보유량을 contents에 저장
		int contents = userStockMapper.selectOneUserStock(userStockDto).getStock_cnt();
		
		resultMap.put("response", response);
		resultMap.put("contents", contents);
		
		return resultMap;
	}
}
