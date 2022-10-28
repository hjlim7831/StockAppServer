package com.search.recentRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchRecordService {
	
	@Autowired
	SearchRecordMapper searchRecordMapper;
	
	public void addResearchRecord(SearchRecordDto searchRecordDto) {
		searchRecordMapper.insertSearchRecord(searchRecordDto);
	}
}