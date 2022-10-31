package com.stock.trade;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.account.UserAccountMapper;
import com.user.info.UserInfoSessionDto;

@Service
public class StockTradeService {
	
	@Resource // session에 저장해 둔 로그인 정보 가져오기
	private UserInfoSessionDto userInfoSessionDto;

	@Autowired
	UserAccountMapper userAccountMapper;
	
	public Map<String, Object> trade(StockTradeDto stockTradeDto) {
		int trade_id = stockTradeDto.getTrade_id();
		String stock_code = stockTradeDto.getStock_code();
		int share = stockTradeDto.getShare();
		int stock_price = stockTradeDto.getStock_price();
		String user_num = stockTradeDto.getUser_num();
		int balance = userAccountMapper.selectBalanceByNum(user_num);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 만약 매매하고자 하는 주식 수가 0이하 경우
		if (share <= 0) {
			resultMap.put("response", "failure_zero_trade");
			resultMap.put("contents", "거래할 주식의 양을 입력해 주세요.");
		} else {
			if (trade_id == 0) { // 매수
				// 사고자 하는 주식 합계 금액보다 통장 잔고가 적은 경우
				if (balance < (stock_price*share)) {
					resultMap.put("response", "failure_lack_of_balance");
					resultMap.put("contents", "통장 잔고가 부족합니다.");
				} else {
					// 통장 잔고 삭감
					stockTradeDto.setAfter_trade_balance(balance - (stock_price*share));
					
					// 보유 주식에 추가
				}
			} else if (trade_id == 1) { // 매도
				// 보유 주식보다 팔고자 하는 주식이 더 많은 경우
				if (true) {
					resultMap.put("response", "failure_lack_of_stock");
					resultMap.put("contents", "보유 주식이 부족합니다.");
				} else {
					// 보유 주식 삭감
					// 통장 잔고 추가
				}
			} else {
				resultMap.put("response", "failure_wrong_trade_id");
				resultMap.put("contents", "거래 방식이 매수(0) 또는 매도(1)가 아닙니다.");
			}
		}

		return resultMap;
	}
}
