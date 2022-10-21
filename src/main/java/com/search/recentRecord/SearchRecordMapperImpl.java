package com.search.recentRecord;

import java.util.ArrayList;

import javax.annotation.Resource;

public class SearchRecordMapperImpl implements SearchRecordMapper {
	
	@Resource
	private SearchRecordSessionDto searchRecordSessionDto;
	
	@Override
	public void insertSearchRecord(SearchRecordDto searchRecordDto) {
		if (searchRecordSessionDto.getSearchRecordDtoList() == null) {
			searchRecordSessionDto.setSearchRecordDtoList(new ArrayList<>());
		}
		
		searchRecordSessionDto.getSearchRecordDtoList().add(0, searchRecordDto);
	}
}