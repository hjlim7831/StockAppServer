package com.search.recentRecord;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchRecordDto {

	private String keyword;
	private String date;
}