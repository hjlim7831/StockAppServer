package com.search.viewCntRank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.detail.dto.StockDto;

@Service
public class ViewCntService {

	@Autowired
	ViewCntRankMapper viewCntRankMapper;

	public List<StockDto> getViewCntTop5StockList() {
		return viewCntRankMapper.selectViewCntTop5Stock();
	}

}
