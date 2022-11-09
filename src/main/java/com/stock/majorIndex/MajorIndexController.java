package com.stock.majorIndex;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class MajorIndexController {
	
	@Autowired
	MajorIndexService majorIndexService;
	
	@GetMapping("major-index")
	public Map<String, Object> getRealtimeMajorIndex(){
		return majorIndexService.realtimeMajorIndex();
	}
	
}
