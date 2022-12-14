package com.user.rank;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("user")
@Api(tags = {"User Rank Controller"}, description = "사용자 랭킹 관련 API")
public class UserRankController {
	
	@Autowired
	UserRankService userRankService;
	
	@GetMapping("rank")
	public Map<String, Object> lookupRank() {
		return userRankService.lookupRank();
	}
	
	@GetMapping("test")
	public void test() {
		userRankService.settingRank();
		return;
	}
}