package com.data.stock;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KospiStockDto {

  private String no;
  private String stockName;
  private String price;                // 현재가
  private String diffAmount;           // 전일비
  private String dayRange;             // 등락률
  private String parValue;             // 액면가
  private String marketCap;            // 시가총액
  private String numberOfListedShares; // 상장 주식 수
  private String foreignOwnRate;       // 외국인 비율
  private String turnover;             // 거래량
  private String per;                  // per
  private String roe;                  // roe
  private String discussionRoomUrl;    // 토론방 url

  public String getDiscussionRoomUrl() {
    return "https://finance.naver.com"+discussionRoomUrl;
  }
}