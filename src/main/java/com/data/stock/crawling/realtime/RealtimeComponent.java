package com.data.stock.crawling.realtime;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.data.stock.crawling.realtime.dto.RealtimeExchangeDto;
import com.data.stock.crawling.realtime.dto.RealtimePriceDto;

@Component
public class RealtimeComponent {

	/**
	 * 실시간 주가를 네이버 증권 API에서 가져오는 함수
	 * 
	 * @param stock_code 실시간 주가를 가져올 주식 종목의 코드
	 * **/
	public RealtimePriceDto getRealtimePrice(String stock_code) {
		// 실시간 주가를 가져올 URL
		String url = "http://api.finance.naver.com/service/itemSummary.nhn?itemcode=" + stock_code;
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders header = new HttpHeaders(); // header 객체 생성
		HttpEntity<?> entity = new HttpEntity<>(header);
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
		
		// uri로 요청 보내고, 응답 받아오기
		ResponseEntity<RealtimePriceDto> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, RealtimePriceDto.class);
		
		// Body에 있는 데이터만 가져와 RealtimeExchangeDto 배열에 저장
		RealtimePriceDto realtimePriceDto = resultMap.getBody();

        return realtimePriceDto;
	}
	
	/**
	 * 실시간 환율 정보를 하나은행 API에서 가져오는 함수
	 * **/
	public RealtimeExchangeDto[] getRealtimeExchange() {
		// 실시간 환율(달러 : USD, 엔화 : JPY, 유로 : EUR, 위안 : CNY)을 가져올 URL
		String url = "https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD,FRX.KRWJPY,FRX.KRWEUR,FRX.KRWCNY";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders header = new HttpHeaders(); // header 객체 생성
		HttpEntity<?> entity = new HttpEntity<>(header);
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
		
		// uri로 요청 보내고, 응답 받아오기
		ResponseEntity<RealtimeExchangeDto[]> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, RealtimeExchangeDto[].class);
		
		// Body에 있는 데이터만 가져와 RealtimeExchangeDto 배열에 저장
		RealtimeExchangeDto[] RealtimeExchangeDto = resultMap.getBody();
		
        return RealtimeExchangeDto;
	}
}