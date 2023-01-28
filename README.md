# Stock App &#128200; Backend

## 목차
1. [개발 환경](#개발-환경)
2. [Backend 팀원](#Backend-팀원)
3. [주요 기능](#주요-기능)
4. [API 설계](#API-설계)
5. [스프링 부트 폴더 구조](#스프링-부트-폴더-구조)
5. [세부 구현 내용](#세부-구현-내용)

## 개발 환경
- Spring Boot 2.7.4
- Spring Tools 3 for Eclipse
- Java 8
- MyBatis 2.2.2
- Gradle 7.5
- MySQL 8.0.21
- Swagger2 2.9.2

## Backend 팀원
- 임혜진 [hjlim7831](https://github.com/hjlim7831)
    1. 요구사항 명세서 작성, API 설계
    2. 자카드 유사도를 통한 주식 종목 검색, 관련 주식 계산 구현
    3. 주식 장 마감 여부 확인 구현
    4. 주식 관련 최신 뉴스, 실시간 주요 지수 크롤링 구현
    5. 추천 알고리즘 구현
    
- 서지윤 [Jeeyoun-S](https://github.com/Jeeyoun-S)
    1. 요구사항 명세서 작성, API 설계
    2. 회원관리, 사용자 랭킹 기능 구현
    3. 주식 및 외환 매매, 실시간 주가 조회, 찜 목록 관리 구현
    4. 추천 알고리즘 구현

## 주요 기능

#### 기본 기능
- 회원관리
- 주요 지수, 주식 정보 조회
- 주식 찜 목록 관리
- 주식 종목 검색

#### 핵심 기능
- 실시간 주가, 환율 정보 조회
- 주식 관련 뉴스 크롤링
- 주식 및 외환 매매
- 주식 종목 추천 알고리즘


## API 설계
- RESTful API
- [API Docs](https://keen-tarsal-f3c.notion.site/API-Docs-4dcd35b711d74b60ab57dbf51159a565)

## 스프링 부트 폴더 구조
```
com
├─ currency
 |	└─ exchange
├─ data
 |	├─ api
 |	└─ stock
 |		├─ crawling
 |		 |	├─ news
 |		 |	└─ realtime
 |		└─ relations
├─ search
 |	├─ recentRecord
 |	├─ result
 |	└─ viewCntRank
├─ stock
 |	├─ category
 |	├─ debut
 |	├─ detail
 |	├─ majorIndex
 |	├─ recommend
 |	└─ trade
├─ user
 |	├─ account
 |	├─ info
 |	├─ rank
 |	└─ stock
 |		├─ holding
 |		└─ wish
└─ interceptor
```

## 세부 구현 내용

### 기본 기능
#### 1. 자카드 유사도를 이용한 주식 종목 검색 기능
$$(자카드 유사도)=J(A,B)={{|A \cap B|}\over {|A \cup B|}}$$
- 주식 종목명과 검색어 간의 자카드 유사도를 계산해 유사도가 높은 검색 결과 노출
- 유사도 계산 시, 중복을 허용하는 다중 집합에 대해 확장해 계산
- [참고 링크](https://school.programmers.co.kr/learn/courses/30/lessons/17677)

#### 2. 주식 세부 정보 조회
- MyBatis로 DB에서 주식 종목별 세부 정보를 조회
- 최근 30일간의  시가, 종가, 최고가, 최저가를 조회해 그래프 데이터 구성
- 업종이 같은 주식 중 주요 제품으로 자카드 유사도를 계산하여 상위 두 개의 주식 종목을 관련 주식으로 제공

#### 3. 회원관리
- 회원가입, 로그인, 로그아웃 구현
- 정규표현식을 이용해 정보 유효성 검사 실시
- 로그인한 정보를 Session에 저장해 사용

#### 4. 주식 찜 목록 관리
- MyBatis를 이용해 찜 목록 테이블에서 조회, 추가, 삭제하는 방식으로 구현

### 핵심 기능
#### 1. 실시간 주가, 환율, 주요 지수 정보 조회
- Rest Template과 Input Stream을 이용해 실시간 정보를 크롤링

#### 2. 주식 관련 뉴스 크롤링
- 네이버 증권에서 제공하는 주식 종목별 뉴스를 jsoup 라이브러리를 이용하여 크롤링

#### 3. 주식 및 외환 매매
- 실시간 주가 정보를 불러와 Session에 저장하고, 일정 시간 내에만 저장된 주가로 거래할 수 있게 구현
- 주식 종목과 거래량을 입력받아 매매 가능 여부를 판단하고, MyBatis로 거래 후 잔고를 DB에 반영

#### 4. 주식 종목 추천 알고리즘
1. 사용자 상황을 고려해 추천 방식 선택  
    - 사용자 로그인 여부, 전체 이용자 수, 보유/관심 주식 현황으로 사용자 상황 분류  
    - **추천 방식**으로는 조회수 높은 주식, 찜이 많은 주식, 관련 주식, **추천 주식** 구현  
    - **추천 주식**은 주식 보유 또는 찜 추가 여부로 타니모토 유사도를 계산해 아이템 기반 협업 필터링으로 구현  

$$(타니모토유사도)=T(A,B)= {{A \cdot B}\over {\lVert A \rVert}^2+{\lVert B \rVert}^2 - A \cdot B}$$

2. 주식 장 마감 여부에 따라 구현 방식 변경  
    - Scheduler로 장 마감 10분 전 유사도를 계산해 Json 파일로 저장  
    - 장이 닫혀있는 경우는 저장해 둔 유사도를 바탕으로, 장이 열려있는 경우는 실시간으로 계산한 유사도를 바탕으로 추천 알고리즘 구현  

### 부가 기능
#### 1. 사용자 수익률 랭킹 조회
- Scheduler를 이용해 매일 자정 사용자의 랭킹 업데이트
- 전날, 당일 총자산을 DB에 저장해 전날 대비 수익률 계산

#### 2. 주식 장 마감 여부 확인
- 국내 주식 휴장일 기준  
    1) 공휴일 (일요일 포함)  
    2) 근로자의 날  
    3) 토요일  
    4) 12월 31일 (공휴일 또는 토요일일 경우 직전의 매매 거래일)  
- 공휴일 정보는 공공데이터포털 한국천문연구원 특일 정보 API를 통해 1년 단위로 받아 JSON에 저장하여 관리
- 정규시장의 매매 거래 시간 - 9:00 ~ 15:30

