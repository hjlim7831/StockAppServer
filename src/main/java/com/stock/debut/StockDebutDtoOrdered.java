package com.stock.debut;

import java.util.List;

import lombok.Data;

@Data
public class StockDebutDtoOrdered {
	private String date;                // 상장 예정 날짜
	private List<StockDebutDto> stocks; // date에 상장 예정인 주식 목록
}
