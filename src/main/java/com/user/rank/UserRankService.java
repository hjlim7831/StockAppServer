package com.user.rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.data.stock.crawling.realtime.RealtimeComponent;
import com.data.stock.crawling.realtime.dto.RealtimeExchangeDto;
import com.user.info.UserInfoSessionDto;
import com.user.rank.dto.UserAccountDto;
import com.user.rank.dto.UserHoldingStockDto;
import com.user.rank.dto.UserRankDto;

@Service
public class UserRankService {
	
	@Autowired
	UserRankMapper userRankMapper;
	
	@Autowired
	RealtimeComponent realtimeComponent;
	
	@Resource // session에 저장해 둔 사용자 정보 가져오기
	UserInfoSessionDto userInfoSessionDto;
	
	@Scheduled(cron = "0 0 0 * * *")
	public void settingRank() {
		
		// 우선 보유주식에 있는 모든 주식의 종가 데이터를 API로부터 가져온다.
		Map<String, Double> nowPriceMap = new HashMap<>();
		List<String> allHoldingStockCode = userRankMapper.selectAllHoldingStock();
		
		for (String code : allHoldingStockCode) {
			double now = realtimeComponent.getRealtimePrice(code).getNow();
			nowPriceMap.put(code, now);
		}
		
		// 환율 정보도 미리 불러온다.
		RealtimeExchangeDto[] realtimeExchangeDto = realtimeComponent.getRealtimeExchange();
				
		// 유저를 전부 불러와서 각 유저의 수익률을 계산한다.
		List<UserRankDto> userRankList = new ArrayList<>();
		List<String> allUserNum = userRankMapper.selectAllUser();
		
		for (String num : allUserNum) {
			double total = 0;
			
			// 통장 정보 가져와 환율 정보와 합쳐
			UserAccountDto userAccountDto = userRankMapper.selectUserAccount(num);
			total += userAccountDto.getBalance();
			total += userAccountDto.getUs()*(realtimeExchangeDto[0].getBasePrice()/realtimeExchangeDto[0].getCurrencyUnit());
			total += userAccountDto.getJpy()*(realtimeExchangeDto[1].getBasePrice()/realtimeExchangeDto[1].getCurrencyUnit());
			total += userAccountDto.getEuro()*(realtimeExchangeDto[2].getBasePrice()/realtimeExchangeDto[2].getCurrencyUnit());
			total += userAccountDto.getYuan()*(realtimeExchangeDto[3].getBasePrice()/realtimeExchangeDto[3].getCurrencyUnit());
			
			// 보유 주식 정보 가져와 현재 가격 계산
			List<UserHoldingStockDto> userHoldingStockDtoList = userRankMapper.selectUserHoldingStock(num);
			for (UserHoldingStockDto userHoldingStockDto : userHoldingStockDtoList) {
				total += userHoldingStockDto.getStock_cnt()*nowPriceMap.get(userHoldingStockDto.getStock_code());
			}
			
			// userRankList에 넣기
			UserRankDto userRankDto = new UserRankDto();
			userRankDto.setUser_num(num);
			userRankDto.setRate((total-5000000)/5000000);
			userRankDto.setTotal_asset(total);
			userRankDto.setTotal_asset_prev(userRankMapper.selectTotalAsset(num));
			userRankList.add(userRankDto);
		}
		
		// comparator로 정렬하기
		Collections.sort(userRankList, new RateComparator().reversed());
		
		int nowRank = 0;
		int number = 0;
		double beforeRate = 0;
		boolean start = true;
		
		for (UserRankDto userRankDto : userRankList) {
			double rate = userRankDto.getRate();
			if (start) {
				start = false;
				beforeRate = rate;
				nowRank++;
				number++;
			} else if (beforeRate == rate) {
				number++;
			} else if (beforeRate > rate) {
				nowRank += number;
				beforeRate = rate;
				number = 1;
			}
			
			userRankDto.setMk_rank(nowRank);
			userRankMapper.updateRank(userRankDto);
		}
		
		return;
	}
	
	class RateComparator implements Comparator<UserRankDto> {
		
		@Override
		public int compare(UserRankDto ushd1, UserRankDto ushd2) {
			double earnings_rate1 = ushd1.getRate();
			double earnings_rate2 = ushd2.getRate();
			
			if (earnings_rate1 > earnings_rate2) return 1;
	        else if (earnings_rate1 < earnings_rate2) return -1;
	        return 0;
		}
	}
	
	public Map<String, Object> lookupRank() {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		String user_num = userInfoSessionDto.getUser_num();
		UserRankDto userRankDto = userRankMapper.selectUserRank(user_num);
		
		double total_asset = userRankDto.getTotal_asset();
		double total_asset_prev = userRankDto.getTotal_asset_prev();
		
		userRankDto.setRate((total_asset-5000000)/5000000);
		userRankDto.setCompare_rate((total_asset-total_asset_prev)/total_asset_prev);
		userRankDto.setCompare_diff(total_asset-total_asset_prev);
		
		int userNumber = userRankMapper.selectUserNumber();
		userRankDto.setPercentage((100/userNumber)*userRankDto.getMk_rank());
		
		resultMap.put("response", "success_lookup_rank");
		resultMap.put("contents", userRankDto);
		
		return resultMap;
	}
}
