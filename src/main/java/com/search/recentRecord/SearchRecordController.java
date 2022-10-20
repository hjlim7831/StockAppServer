package com.search.recentRecord;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("search")
public class SearchRecordController {
	
	@Resource(name = "searchRecordSessionDto")
	private SearchRecordSessionDto searchRecordSessionDto;
	
	@GetMapping("record")
	@ResponseBody
	public SearchRecordSessionDto searchRecordLookup() {
		System.out.println(searchRecordSessionDto);
		System.out.println("테스트");
		return searchRecordSessionDto; // 클래스 자료형 내 리스트 JSON 변환, 기록 몇 개까지 남길지 생각
	}
}