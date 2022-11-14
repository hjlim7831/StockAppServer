package com.search.recentRecord;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRecordDto {
	
	private String keyword;      // 검색한 keyword
	private LocalDateTime date;  // 검색한 날짜
}