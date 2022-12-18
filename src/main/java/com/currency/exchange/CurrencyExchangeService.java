package com.currency.exchange;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimeExchangeDto;
import com.user.account.UserAccountMapper;
import com.user.info.UserInfoSessionDto;

@Service
public class CurrencyExchangeService {
	
	@Resource // session에 저장해 둔 로그인 정보 가져오기
	private UserInfoSessionDto userInfoSessionDto;
	
	@Resource // session에 저장할, 또는 가져올 외화 정보
	private CurrencyExchangeSessionDto currencyTradeSessionDto;
	
	@Autowired
	RealtimeComponent realtimeComponent;
	
	@Autowired
	CurrencyExchangeMapper currencyTradeMapper;
	
	@Autowired
	UserAccountMapper userAccountMapper;
	
	
	// 최소 total 설정** & interceptor
	public Map<String, Object> tradeCurrency(String country, int total) {
		
		return exchange(country, total, 0);
	}
	
	public Map<String, Object> tradeWon(String country, int total) {
		
		return exchange(country, total, 1);
	}
	
	private Map<String, Object> exchange(String country, int total, int trade) {
		Map<String, Object> resultMap = new HashMap<>();

		String name = currencyTradeSessionDto.getCountry_name();
		
		// 
		if (trade == 0 && total < 300) {
			resultMap.put("response", "failure_less_than_300");
			resultMap.put("resource", "최소 환전 금액은 300 이상입니다.");
		}
		
		// session에 저장해 둔 외화와 거래하려는 외화가 다른 경우
		else if (name.equals(country)) {
			String user_num = userInfoSessionDto.getUser_num();
			
			double price = currencyTradeSessionDto.getCurrency_price();
			double balance = userAccountMapper.selectBalanceByNum(user_num); // 사용자의 통장 잔고 가져오기
			double currency = userAccountMapper.selectOneCurrency(user_num, country);
			
			if ((trade == 0 && price*total > balance) || (trade == 1 && total > currency)) {
				resultMap.put("response", "failure_lack_of_balance");
				resultMap.put("resource", "통장 잔고가 부족합니다.");
			}
			
			else {
				double rest_balance;
				double rest_currency;
				
				if (trade == 0) {
					rest_balance = balance - (price*total);
					rest_currency = currency + total;
				} else {
					rest_balance = balance + (price*total);
					rest_currency = currency - total;
				}

				int balance_result = userAccountMapper.updateBalanceByUserNum(rest_balance, user_num);
				int currency_result = userAccountMapper.updateCurrencyByUserNum(user_num, country, rest_currency);
				
				if (balance_result == 1 && currency_result == 1) {
					resultMap.put("response", "success_exchange_currency");
					resultMap.put("resource", "원화에서 외화로 환전에 성공했습니다.");
				} else {
					resultMap.put("response", "failure_server_error");
					resultMap.put("resource", "서버 상의 오류로 환전에 실패했습니다. 다시 시도해 주세요.");
				}
			}
			
		} else {
			resultMap.put("response", "failure_please_lookup_price");
			resultMap.put("resource", "조회한 외화와 입력하신 외화가 다릅니다.");
		}
		
		return resultMap;
	}

	public Map<String, Object> tradePrice(String country) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (country.equals("USD") || country.equals("JPY") || country.equals("EUR") || country.equals("CNY")) {
			currencyTradeSessionDto.setCountry_name(country);
			
			RealtimeExchangeDto realtimeExchangeDto = realtimeComponent.getRealtimeExchange(country)[0];
			currencyTradeSessionDto.setCurrency_price(realtimeExchangeDto.getBasePrice());
			
			Map<String, Object> currencyMap = new HashMap<String, Object>();
			currencyMap.put("country_name", country);
			currencyMap.put("currency", realtimeExchangeDto.getBasePrice());
			
			resultMap.put("response", "success_get_trade_price");
			resultMap.put("contents", currencyMap);
		} else {
			resultMap.put("response", "failure_get_trade_price");
			resultMap.put("contents", "존재하지 않는 외화 이름입니다. 입력한 country를 다시 확인해 주세요.");
		}

		return resultMap;
	}

}
