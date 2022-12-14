package com.currency.trade;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimeExchangeDto;

@Service
public class CurrencyTradeService {
	
	@Resource // session에 저장할, 또는 가져올 오화 정보
	private CurrencyTradeSessionDto currencyTradeSessionDto;
	
	@Autowired
	RealtimeComponent realtimeComponent;
	
	@Autowired
	CurrencyTradeMapper currencyTradeMapper;
	
	public Map<String, Object> tradeCurrency(String country, int total) {
		
		
		return null;
	}
	
	public Map<String, Object> tradeWon(String country, int total) {
		
		
		return null;
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
