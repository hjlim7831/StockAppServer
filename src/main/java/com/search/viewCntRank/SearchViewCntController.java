package com.search.viewCntRank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.search.result.SearchResultDto;

@RestController
@RequestMapping("search")
public class SearchViewCntController {

	@Autowired
	SearchViewCntService viewCntService;

	@GetMapping("view-cnt-ranking")
	public Map<String, Object> viewCntRankTop5() {
		Map<String, Object> resultMap = new HashMap<>();
		
		List<SearchResultDto> contents = viewCntService.getViewCntTop5StockList();
		String response = "success_get_view_cnt_rank_top5";
		
		resultMap.put("contents",contents);
		resultMap.put("response", response);
		
		return resultMap;
	}

}
