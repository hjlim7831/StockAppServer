package com.search.recentRecord;

import javax.annotation.Resource;

public class SearchRecordMapperImpl implements SearchRecordMapper {
	
	@Resource
	private SearchRecordSessionDto searchRecordSessionDto;
	
	@Override
	public void insertSearchRecord(SearchRecordDto searchRecordDto) {
		searchRecordSessionDto.getSearchRecordDto().add(0, searchRecordDto);;
	}
}