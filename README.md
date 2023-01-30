# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 1단계 - 지하철역 인수 테스트 작성
### 요구사항
- [x] 지하철역 인수 테스트를 완성하세요.
  - [x] 지하철역 목록 조회 인수 테스트 작성하기
  - [x] 지하철역 삭제 인수 테스트 작성하기

## 🚀 2단계 - 지하철 노선 관리
### 요구사항
- [x] 지하철 노선 생성
  - When 지하철 노선을 생성하면
  - Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
- [x] 지하철 노선 목록 조회
  - Given 2개의 지하철 노선을 생성하고
  - When 지하철 노선 목록을 조회하면
  - Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
- [x] 지하철 노선 조회
  - Given 지하철 노선을 생성하고
  - When 생성한 지하철 노선을 조회하면
  - Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
- [x] 지하철 노선 수정
  - Given 지하철 노선을 생성하고
  - When 생성한 지하철 노선을 수정하면
  - Then 해당 지하철 노선 정보는 수정된다
- [x] 지하철 노선 삭제
  - Given 지하철 노선을 생성하고
  - When 생성한 지하철 노선을 삭제하면
  - Then 해당 지하철 노선 정보는 삭제된다

## 🚀 3단계 - 지하철 구간 관리
### 요구사항
- [x] 구간 등록 기능
  - [x] 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
  - [x] 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
  - [x] 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
- [x] 구간 제거 기능
  - [x] 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
  - [x] 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
  - [x] 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.