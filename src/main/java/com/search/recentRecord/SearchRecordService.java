package com.search.recentRecord;

import org.springframework.beans.factory.annotation.Autowired;

public class SearchRecordService {
	
	@Autowired
	SearchRecordMapper searchRecordMapper;
	
	public void addResearchRecord(SearchRecordDto searchRecordDto) {
		searchRecordMapper.insertSearchRecord(searchRecordDto);
	}
}