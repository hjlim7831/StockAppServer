package com.search.recentRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchRecordController {

	@Resource
	private SearchRecordSessionDto searchRecordSessionDto;

	@GetMapping("record") // 최근 검색 목록 조회
	public Map<String, Object> searchRecordLookup() {
		Map<String, Object> resultMap = new HashMap<>();
		
		if (searchRecordSessionDto.getSearchRecordDtoList() == null) { // 만약 검색한 적이 없다면
			searchRecordSessionDto.setResponse("failure_no_record");   // response를 failure로 수정
		} 
		
		// session에 저장해 둔 검색 목록(contents)와 응답(response) 가져오기
		List<SearchRecordDto> contents = searchRecordSessionDto.getSearchRecordDtoList();
		String response = searchRecordSessionDto.getResponse();
		
		resultMap.put("contents", contents);
		resultMap.put("response", response);
		
		return resultMap;
	}
}