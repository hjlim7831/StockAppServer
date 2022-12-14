package com.user.rank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRankDto {
	
	@JsonIgnore
	private String user_num;
	
	@ApiModelProperty(value = "순위")
	private int mk_rank;
	
	@ApiModelProperty(value = "수익률")
	private double rate;
	
	@ApiModelProperty(value = "오늘 총 자신")
	private double total_asset;
	
	@ApiModelProperty(value = "전날 총 자산")
	private double total_asset_prev;
	
	@ApiModelProperty(value = "전날 대비 오늘 총 자산 등락률")
	private double compare_rate;
	
	@ApiModelProperty(value = "전날 총 자산 - 오늘 총 자산")
	private double compare_diff;
	
	@ApiModelProperty(value = "백분율")
	private double percentage;
}