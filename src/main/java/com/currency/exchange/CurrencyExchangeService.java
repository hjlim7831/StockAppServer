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
	UserAccountMapper userAccountMapper;
	
	
	/**
	 * 원화  → 외화 환전하기
	 * @param country 외화명
	 * @param total 환전할 외화 긍액
	 * **/
	public Map<String, Object> tradeCurrency(String country, int total) {
		
		return exchange(country, total, 0);
	}
	
	/**
	 * 외화  → 원화 환전하기
	 * @param country 외화명
	 * @param total 환전할 외화 긍액
	 * **/
	public Map<String, Object> tradeWon(String country, int total) {
		
		return exchange(country, total, 1);
	}
	
	/**
	 * 환전하기
	 * @param country 외화명
	 * @param total 환전할 외화 긍액
	 * @param trade 0이면 외화로, 1이면 원화로 환전
	 * **/
	private Map<String, Object> exchange(String country, int total, int trade) {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		// session에 환율과 함께 저장해 둔 외화 이름
		String name = currencyTradeSessionDto.getCountry_name();
		
		// 만약 외화로 환전 시 최소 금액을 충족하지 못하는 경우
		if (trade == 0 && (country.equals("USD") && total < 100)) { // 달러
			resultMap.put("response", "failure_less_than_100_USD");
			resultMap.put("contents", "최소 환전 금액은 100 달러입니다.");
		} else if (trade == 0 && (country.equals("JPY") && total < 13500)) { // 엔화
			resultMap.put("response", "failure_less_than_13500_JPY");
			resultMap.put("contents", "최소 환전 금액은 13500 엔화입니다.");
		} else if (trade == 0 && (country.equals("EUR") && total < 100)) { // 유로
			resultMap.put("response", "failure_less_than_100_EUR");
			resultMap.put("contents", "최소 환전 금액은 100 유로입니다.");
		} else if (trade == 0 && (country.equals("CNY") && total < 700)) { // 위안
			resultMap.put("response", "failure_less_than_700_CNY");
			resultMap.put("contents", "최소 환전 금액은 700 위안입니다.");
		} else if (trade == 1 && total <= 0) {
			resultMap.put("response", "failure_exchange_zero");
			resultMap.put("contents", "환전 금액은 0 이상으로 입력해 주세요.");
		}
		
		// session에 저장해 둔 외화와 거래하려는 외화가 다른 경우
		else if (!(name == null) && name.equals(country)) {
			
			// session에 저장해 둔 로그인 정보 가져오기
			String user_num = userInfoSessionDto.getUser_num();
			
			// session에 저장해 둔 환율 가져오기
			double price = currencyTradeSessionDto.getCurrency_price();
			
			// 사용자의 통장 잔고 가져오기
			double balance = userAccountMapper.selectBalanceByNum(user_num);
			
			// 사용자의 해당 외화 보유 금액 가져오기
			double currency = userAccountMapper.selectOneCurrency(user_num, country);
			
			// 만약 환전 금액이 부족한 경우라면
			if ((trade == 0 && price*total > balance) || (trade == 1 && total > currency)) {
				resultMap.put("response", "failure_lack_of_balance");
				resultMap.put("contents", "통장 잔고가 부족합니다.");
			}
			
			else {

				double rest_balance;  // 환전 이후 원화 잔액
				double rest_currency; // 환전 이후 외화 잔액
				
				if (trade == 0) {
					rest_balance = balance - (price*total);
					rest_currency = currency + total;
				} else {
					rest_balance = balance + (price*total);
					rest_currency = currency - total;
				}
				
				// 환전 결과인 원화와 외화를 DB에 반영
				int balance_result = userAccountMapper.updateBalanceByUserNum(rest_balance, user_num);
				int currency_result = userAccountMapper.updateCurrencyByUserNum(user_num, country, rest_currency);
				
				if (balance_result == 1 && currency_result == 1) {
					
					if (trade == 0) resultMap.put("contents", "원화에서 외화로 환전에 성공했습니다.");	
					else resultMap.put("contents", "외화에서 원화로 환전에 성공했습니다.");	
					
					resultMap.put("response", "success_exchange_currency");
				} else {
					resultMap.put("response", "failure_server_error");
					resultMap.put("contents", "서버 상의 오류로 환전에 실패했습니다. 다시 시도해 주세요.");
				}
			}
			
		}
		
		// session에 저장된 외화와 환전하려는 외화가 다른 경우
		else {
			resultMap.put("response", "failure_please_lookup_price");
			resultMap.put("contents", "조회한 외화와 입력하신 외화가 다릅니다.");
		}
		
		return resultMap;
	}
	
	/**
	 * 환율 조회하기
	 * @param country 외화명
	 * **/
	public Map<String, Object> tradePrice(String country) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 외화명이 USD, JPY, EUR, CNY 중 하나인 경우
		if (country.equals("USD") || country.equals("JPY") || country.equals("EUR") || country.equals("CNY")) {
			
			// session에 외화명 넣어주기
			currencyTradeSessionDto.setCountry_name(country);
			
			// 외화명에 해당하는 환율 가져오고, session에 넣어주기
			RealtimeExchangeDto realtimeExchangeDto = realtimeComponent.getRealtimeExchange(country)[0];
			currencyTradeSessionDto.setCurrency_price(realtimeExchangeDto.getBasePrice());
			
			Map<String, Object> currencyMap = new HashMap<String, Object>();
			currencyMap.put("country", country);
			currencyMap.put("currency", realtimeExchangeDto.getBasePrice());
			
			resultMap.put("response", "success_get_trade_price");
			resultMap.put("contents", currencyMap);
		}
		
		// 외화명이 범위에서 벗어난 경우
		else {
			resultMap.put("response", "failure_get_trade_price");
			resultMap.put("contents", "존재하지 않는 외화 이름입니다. 입력한 country를 다시 확인해 주세요.");
		}

		return resultMap;
	}

}
