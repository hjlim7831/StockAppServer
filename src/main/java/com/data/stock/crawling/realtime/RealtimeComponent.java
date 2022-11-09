package com.data.stock.crawling.realtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.data.stock.crawling.realtime.dto.RealtimeExchangeDto;
import com.data.stock.crawling.realtime.dto.RealtimeMajorIndexDto;
import com.data.stock.crawling.realtime.dto.RealtimePriceDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class RealtimeComponent {
	

	/**
	 * 실시간 주가를 네이버 증권 API에서 가져오는 함수
	 * 
	 * @param stock_code 실시간 주가를 가져올 주식 종목의 코드
	 * **/
	public RealtimePriceDto getRealtimePrice(String stock_code) throws JsonProcessingException {
		// 실시간 주가를 가져올 URL
		String url = "http://api.finance.naver.com/service/itemSummary.nhn?itemcode=" + stock_code;
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders header = new HttpHeaders(); // header 객체 생성
		HttpEntity<?> entity = new HttpEntity<>(header);
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
		
		// uri로 요청 보내고, 응답 받아오기
		ResponseEntity<RealtimePriceDto> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, RealtimePriceDto.class);
		
		// Body에 있는 데이터만 가져와 RealtimeExchangeDto 배열에 저장
		RealtimePriceDto realtimeDto = resultMap.getBody();
		
        return realtimeDto;
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
	/**
	 * 실시간 주요 지수들을 가져오는 함수
	 * @return KOSPI, KOSDAQ, KPI200을 DTO array로 담아 전달
	 */
	public RealtimeMajorIndexDto[] getMajorIndexRealtime() {
		// KOSPI, KOSDAQ, KPI200
		String urlLink = "https://polling.finance.naver.com/api/realtime?query=SERVICE_INDEX:KOSPI,KOSDAQ,KPI200";
		RealtimeMajorIndexDto[] miArr = new RealtimeMajorIndexDto[3];
		
		try {
			URL url = new URL(urlLink);

			InputStream input = url.openStream();
			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder jsonString = new StringBuilder();
			int c;
			while ((c = reader.read()) != -1) {
				jsonString.append((char) c);
			}
			Gson gson = new Gson();
			JsonObject json = gson.fromJson(jsonString.toString(), JsonObject.class);
			JsonArray result = json.getAsJsonObject("result").getAsJsonArray("areas").get(0).getAsJsonObject()
					.getAsJsonArray("datas");
			for (int i = 0; i < 3; i++) {
				miArr[i] = gson.fromJson(result.get(i),RealtimeMajorIndexDto.class);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return miArr;

	}
}