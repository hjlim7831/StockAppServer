package com.search.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.detail.StockDto;

@RestController
@RequestMapping("search")
public class SearchResultController {

	@Autowired
	SearchResultService searchResultService;

	@GetMapping("result")
	public List<StockDto> searchResult(Model model, String keyWord) {
		return searchResultService.getSearchResultList(keyWord);
		
	}

}
