package com.stock.trade;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimePriceDto;
import com.user.account.UserAccountMapper;
import com.user.info.UserInfoSessionDto;
import com.user.stock.holding.UserStockHoldingDto;
import com.user.stock.holding.UserStockHoldingMapper;

@Service
public class StockTradeService {
	
	@Resource // session에 저장해 둔 로그인 정보 가져오기
	private UserInfoSessionDto userInfoSessionDto;
	
	@Resource // session에 저장할, 또는 가져올 주가 정보
	private StockTradeSessionDto stockTradeSessionDto;
	
	@Autowired
	UserAccountMapper userAccountMapper;
	
	@Autowired
	UserStockHoldingMapper userStockHoldingMapper;
	
	@Autowired
	RealtimeComponent realtimeComponent;
	
	public Map<String, Object> trade(StockTradeDto stockTradeDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>(); // 응답 저장하기
		
		int trade_id = stockTradeDto.getTrade_id();                   // 매도(1)인지 매수(0)인지
		String stock_code = stockTradeDto.getStock_code();            // 거래할 주식 코드
		int share = stockTradeDto.getShare();                         // 거래할 주식 개수
		
		if (share <= 0) { // 만약 매매하고자 하는 주식 수가 0이하 경우
			resultMap.put("response", "failure_zero_trade");
			resultMap.put("contents", "거래할 주식의 수를 0 이상으로 입력해 주세요.");
			
			return resultMap;
		}

		double stock_price = stockTradeSessionDto.getStock_price();      // session에 저장해둔 stock_code의 주식 가격 가져오기
		String user_num = userInfoSessionDto.getUser_num();              // session에 저장해둔 사용자 고유번호 가져오기
		double balance = userAccountMapper.selectBalanceByNum(user_num); // 사용자의 통장 잔고 가져오기
		
		// DB에 있는 user_num, stock_code에 해당하는 정보 가져오기
		UserStockHoldingDto userStockHoldingDto = userStockHoldingMapper.selectOneUserStock(user_num, stock_code);
		
		double now_price = 0; // 보유 주식 구매 시 가격
		int stock_cnt = 0;    // 보유 주식 수
		
		if (trade_id == 0) { // 매수
			if (balance < (stock_price*share)) { // 사고자 하는 주식 합계 금액보다 통장 잔고가 적은 경우
				resultMap.put("response", "failure_lack_of_balance");
				resultMap.put("contents", "통장 잔고가 부족합니다.");
				
				return resultMap;
			}
			
			// 현재 사용자가 보유 중인 주식 현황 저장하기
			if (userStockHoldingDto != null) { // DB 보유 주식 목록에 있다면 해당 값 가져오기
				now_price = userStockHoldingDto.getNow_price();
				stock_cnt = userStockHoldingDto.getStock_cnt();
			}
			
			// stock_cnt 및 now_price 반영
			stockTradeDto.setStock_cnt(stock_cnt + share);
			stockTradeDto.setNow_price(now_price + (stock_price*share));
			stockTradeDto.setUser_num(user_num);
			
			if (userStockHoldingDto == null) userStockHoldingMapper.insertOneUserStock(stockTradeDto);
			else userStockHoldingMapper.updateOneUserStock(stockTradeDto);
			
			// balance 반영
			userAccountMapper.updateBalanceByUserNum(balance - (stock_price*share), user_num);
			
			resultMap.put("response", "success_to_buy");
			resultMap.put("contents", "매수가 완료됐습니다.");
		
		} else if (trade_id == 1) { // 매도
			if (userStockHoldingDto == null || userStockHoldingDto.getStock_cnt() < share) {
				resultMap.put("response", "failure_lack_of_stock");
				resultMap.put("contents", "보유 주식이 부족해 매도가 불가능합니다.");
				
				return resultMap;
			}
			
			now_price = userStockHoldingDto.getNow_price();
			stock_cnt = userStockHoldingDto.getStock_cnt();
			
			// stock_cnt 및 now_price 반영
			if (stock_cnt - share == 0) userStockHoldingMapper.deleteOneUserStock(user_num, stock_code);
			else {
				stockTradeDto.setStock_cnt(stock_cnt - share);
				stockTradeDto.setNow_price(now_price - ((now_price/stock_cnt)*share));
				stockTradeDto.setUser_num(user_num);
				
				userStockHoldingMapper.updateOneUserStock(stockTradeDto);
			}
			
			// balance 반영
			userAccountMapper.updateBalanceByUserNum(balance - (stock_price*share), user_num);
			
			resultMap.put("response", "success_to_sell");
			resultMap.put("contents", "매도가 완료됐습니다.");
			
		} else { // 매수도 매도도 아닌 경우
			resultMap.put("response", "failure_wrong_trade_id");
			resultMap.put("contents", "거래 방식은 매수 또는 매도를 선택해 주세요.");
		}

		return resultMap;
	}

	public Map<String, Object> tradePrice(String stock_code) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 실시간 주가 정보 가져오기
		RealtimePriceDto realtimePriceDto = realtimeComponent.getRealtimePrice(stock_code);
		
		if (realtimePriceDto == null) { // 가져올 주가 정보가 없는 경우
			resultMap.put("response", "failure_to_find_stock");
			resultMap.put("contents", "존재하지 않는 주식 종목입니다.");
		} else { // 주가 정보를 가져온 경우
			double now = realtimePriceDto.getNow();
			
			stockTradeSessionDto.setStock_price(now); // 세션에 stock_price 저장
			stockTradeSessionDto.setStock_code(stock_code); // 세션에 stock_code 저장
			
			Map<String, Object> priceMap = new HashMap<String, Object>();
			priceMap.put("now", now);
			
			resultMap.put("response", "success_get_trade_price");
			resultMap.put("contents", priceMap);
		}

		return resultMap;
	}
}
