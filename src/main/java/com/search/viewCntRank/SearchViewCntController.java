package com.search.viewCntRank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.search.result.SearchResultDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("search")
@Api(tags = {"Search Controller"}, description = "검색 관련 API")
public class SearchViewCntController {

	@Autowired
	SearchViewCntService viewCntService;

	@GetMapping("view-cnt-ranking")
	@ApiOperation(value = "조회수 TOP5 주식 조회")
	public Map<String, Object> viewCntRankTop5() {
		Map<String, Object> resultMap = new HashMap<>();
		
		List<SearchResultDto> contents = viewCntService.getViewCntTop5StockList();
		String response = "success_get_view_cnt_rank_top5";
		
		resultMap.put("contents",contents);
		resultMap.put("response", response);
		
		return resultMap;
	}

}
