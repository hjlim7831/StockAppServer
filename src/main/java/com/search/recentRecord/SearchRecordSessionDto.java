package com.search.recentRecord;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Component
@Data
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SearchRecordSessionDto implements Serializable {
	
	// session에 저장할 최근 검색 목록
	
	@JsonIgnore
	static final long serialVersionUID = 1L;
	
	private String response; // 응답 (검색 기록 있으면 success, 없으면 failure)
	private List<SearchRecordDto> searchRecordDtoList; // 최근 검색 목록
}