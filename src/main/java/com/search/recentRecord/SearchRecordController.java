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
		
		return searchRecordSessionDto; // Ŭ���� �ڷ��� �� ����Ʈ JSON ��ȯ, ��� �� ������ ������ ����
	}
}