package com.search.recentRecord;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchRecordController {
	
	@Resource
	private SearchRecordSessionDto searchRecordSessionDto;
	
	@GetMapping("record")
	public SearchRecordSessionDto searchRecordLookup() {
		System.err.println(1);
		System.out.println(searchRecordSessionDto);
		System.out.println(2);
		
		if (searchRecordSessionDto.getSearchRecordDtoList() == null) {
			searchRecordSessionDto.setSearchRecordDtoList(new ArrayList<>());
		}
		
		return searchRecordSessionDto; // 클래스 자료형 내 리스트 JSON 변환, 기록 몇 개까지 남길지 생각
	}
}