package com.data.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpecialDayComponent {
	
	@Value("${spcd.apikey}")
	String apiKey;
	
	String url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService";
	
//	public void 
	
	
}
