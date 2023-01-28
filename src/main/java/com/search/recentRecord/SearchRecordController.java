package com.search.recentRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("search")
@Api(tags = {"Search Controller"}, description = "검색 관련 API")
public class SearchRecordController {

	@Resource
	private SearchRecordSessionDto searchRecordSessionDto;

	@GetMapping("record") // 최근 검색 목록 조회
	@ApiOperation(value = "최근 검색 목록 조회")
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