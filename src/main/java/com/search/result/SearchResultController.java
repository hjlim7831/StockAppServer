package com.search.result;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.search.recentRecord.SearchRecordDto;
import com.search.recentRecord.SearchRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("search")
@Api(tags = {"Search Controller"}, description = "검색 관련 API")
public class SearchResultController {

	@Autowired
	SearchResultService searchResultService;

	@Autowired
	SearchRecordService searchRecordService;

	@GetMapping("result")
	@ApiOperation(value = "키워드 검색 결과 조회")
	public Map<String, Object> searchResult(Model model, String keyWord) {
		if (keyWord != null && !keyWord.equals("")) {
			searchRecordService
					.insertSearchRecord(new SearchRecordDto(keyWord, LocalDateTime.now(ZoneId.of("Asia/Seoul"))));
		}
		return searchResultService.getSearchResultList(keyWord);
	}

}