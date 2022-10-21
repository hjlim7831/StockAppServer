package com.search.viewCntRank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.detail.dto.StockDto;

@RestController
@RequestMapping("search")
public class ViewCntController {

	@Autowired
	ViewCntService viewCntService;

	@GetMapping("view-cnt-ranking")
	public List<StockDto> viewCntRankTop5() {
		return viewCntService.getViewCntTop5StockList();
	}

}
