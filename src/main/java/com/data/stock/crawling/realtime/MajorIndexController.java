package com.data.stock.crawling.realtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MajorIndexController {
	
	@Autowired
	MajorIndexService majorIndexService;
	
	
	@GetMapping("test1")
	public String getMajorIndex() {
		majorIndexService.getMajorIndexRealtime();
		
		return null;
	}
	
	
}
