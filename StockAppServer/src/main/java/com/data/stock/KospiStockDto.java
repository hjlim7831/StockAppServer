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
  private String price;                // ���簡
  private String diffAmount;           // ���Ϻ�
  private String dayRange;             // �����
  private String parValue;             // �׸鰡
  private String marketCap;            // �ð��Ѿ�
  private String numberOfListedShares; // ���� �ֽ� ��
  private String foreignOwnRate;       // �ܱ��� ����
  private String turnover;             // �ŷ���
  private String per;                  // per
  private String roe;                  // roe
  private String discussionRoomUrl;    // ��й� url

  public String getDiscussionRoomUrl() {
    return "https://finance.naver.com"+discussionRoomUrl;
  }
}