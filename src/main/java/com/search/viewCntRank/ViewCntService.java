package com.search.viewCntRank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.search.result.SearchResultDto;

@Service
public class ViewCntService {

	@Autowired
	ViewCntRankMapper viewCntRankMapper;

	public List<SearchResultDto> getViewCntTop5StockList() {
		return viewCntRankMapper.selectViewCntTop5Stock();
	}

}
