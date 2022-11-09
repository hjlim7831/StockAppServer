package com.stock.majorIndex;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimeMajorIndexDto;

@Service
public class MajorIndexService {

	@Autowired
	RealtimeComponent realtimeComponent;

	public Map<String, Object> realtimeMajorIndex() {
		RealtimeMajorIndexDto[] rmiArr = realtimeComponent.getMajorIndexRealtime();
		
		// "cd값" : { "status": "msVal", "nv": "nvVal", "cv": "cvVal", "cr": "crVal"}
		
		Map<String, Object> totRes = new HashMap<>();
		for (int i = 0; i < 3; i++) {
			RealtimeMajorIndexDto rmi = rmiArr[i];
			String status = rmi.getMs(); // 주식 장 열렸는지 닫혔는지
			String now_price = rmi.getNv(); // 현재 가격
			String diff = rmi.getCv(); // 전일비
			String fluct_rate = rmi.getCr(); // 등락률
			String code = rmi.getCd(); // 주요지수 종류 (코드)
			
			Map<String, String> content = new HashMap<>();
			content.put("status", status);
			content.put("now_price",now_price);
			content.put("diff",diff);
			content.put("fluct_rate",fluct_rate);
			
			totRes.put(code,content);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("response","success_get_major_index");
		resultMap.put("contents", totRes);

		return resultMap;
	}

}
