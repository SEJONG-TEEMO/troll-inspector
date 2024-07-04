## ![favicon-16x16](https://github.com/SEJONG-TEEMO/troll-inspector/assets/37898720/8cabd591-6f93-4cd2-b53f-09090963ffa9) TEEMO.GG

- 리그 오브 레전드 최근 40게임 전적 조회 사이트
- 유저의 솔로 랭크를 조회하여, 게임 플레이를 할 때에 전적을 조회하여 유저들의 게임의 흥미를 돋구도록 기대하며 제작했습니다.

[프로젝트 링크 - 7월 8일 경 서버를 종료할 예정.](https://teemo.kr)
---

## Architecture

![teemo gg](https://github.com/SEJONG-TEEMO/troll-inspector/assets/37898720/7877e60e-2288-440b-ae53-5ec8b5ccefef)

## 개선점

- [크롤링 - 셀레니움의 메모리 누수 해결 및 성능 개선](https://velog.io/@swager253/TEEMO.GG-%EA%B0%9C%EB%B0%9C%EA%B8%B0-%ED%81%AC%EB%A1%A4%EB%9F%AC-%EA%B0%9C%EB%B0%9C)
- [배치 - Bucket4j를 활용하여 외부 API 요청 지연과 트래픽 제어](https://velog.io/@swager253/TEEMO.GG-%EA%B0%9C%EB%B0%9C%EA%B8%B0-%EB%B0%B0%EC%B9%98)

## Git Convention

- feat: 새로운 기능을 추가 할 때
- refactor: 기존의 기능을 리팩토링
- rename: 파일, 패키지, 모듈 등 이름을 변경해야 할 때
- remove: 파일, 패키지, 모듈 등을 삭제해야 할 때
- env: 환경 파일 .yml 을 추가하거나 변경할 때
- docs: 문서 파일을 작성 할 때