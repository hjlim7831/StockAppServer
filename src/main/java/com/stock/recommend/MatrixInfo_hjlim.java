package com.stock.recommend;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatrixInfo_hjlim {
	private List<Boolean[]> matrix;
	private Map<String, Integer> codeMap;
	private List<String> codeIdxList;
	private double[] l2Arr;

}
