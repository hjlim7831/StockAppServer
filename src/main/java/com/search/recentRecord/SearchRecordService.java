package com.search.recentRecord;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class SearchRecordService {
	
	@Resource
	private SearchRecordSessionDto searchRecordSessionDto;
	
	// 검색 시, 최근 검색 목록에 추가
	public void insertSearchRecord(SearchRecordDto searchRecordDto) {
		
		if (searchRecordSessionDto.getSearchRecordDtoList() == null) {          // 이전까지 검색한 적이 없었다면
			searchRecordSessionDto.setResponse("success_lookup_search_record"); // 검색한 적이 있는 것으로 response를 바꾸고,
			searchRecordSessionDto.setSearchRecordDtoList(new ArrayList<>());   // 빈 리스트 생성
		}
		
		// 최신순으로 정렬되도록 리스트의 맨 앞에 이번에 검색한 searchRecordDto를 추가
		searchRecordSessionDto.getSearchRecordDtoList().add(0, searchRecordDto);
	}
}