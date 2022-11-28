package com.stock.detail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockRelationDto {
	private long number;
	private String code;
	private String name;
}
