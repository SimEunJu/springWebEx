[ 개요 ] 

1. 컨셉
오늘 하루를 들려줘
하루 동안 어떤 일이 있었는지 작성하고 공감을 얻는다.

2. 기술
spring MVC, mybatis, mysql, 
- security
- server side oauth
- 통계
- cron
- https
- handlebar
- tiles
- 메일
- long polling
- view, procedure
- spring-session
- custom annotaion 활용한 logging
- travis ci + aws

3. 기능
- 게시글 crud
- 계층형 댓글
- 익명 게시글/댓글
- 비밀 댓글
- 신고, 좋아요
- 파일 업로드
- 관리자 통계/ 메일
- cron 파일 삭제, 통계, 휴면회원 전환 및 메일 발송
- 소셜 로그인
- 회원 관련 업데이트, 삭제 시 로그 남김
- 작성 게시글에 댓글 달릴 시 알림
- 알림 실시간으로 업데이트

4. 문제해결 과정
- serverside oauth
- db 관련: 파일 삭제, procedure, view, 통계
- 로그인이 계속 풀려 spring-session
- 하루 방문 수
