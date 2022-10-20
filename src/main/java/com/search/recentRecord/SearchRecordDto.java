package com.search.recentRecord;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SearchRecordDto {
	private String keyword;
	private String date;

	public SearchRecordDto(String keyword, String date) {
		this.keyword = keyword;
		this.date = date;
	}
}