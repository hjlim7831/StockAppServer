package com.user.rank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserRankDto {
	
	@JsonIgnore
	private String user_num;
	
	private int mk_rank;
	private double rate;
}
