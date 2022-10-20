package com.search.recentRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@ToString
public class SearchRecordSessionDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private String response = "success";
	private List<SearchRecordDto> searchRecordDto = new ArrayList<>();
}