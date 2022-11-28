package com.data.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test2Controller {
	
	@Autowired
	OpeningDateComponent openingDateComponent;
	
	@GetMapping("testApikey")
	public String test() {
		System.out.println(openingDateComponent.isOpeningDate());
		return null;
	}
}
