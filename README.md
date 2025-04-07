# Blog-BE-ItoR

# Blog 만들기

`ex) GET posts/all`

## ERD

![블로그 1주차 erd](https://github.com/user-attachments/assets/db86eb59-bba7-45f0-a559-a92fd5cfc210)

## 요구사항 정의서

![image](https://github.com/user-attachments/assets/834a9589-d526-47f4-aeab-f27f82226723)

## 디렉토리
```
/blog-backend
 ├── src
 │   ├── main
 │   │   ├── java/com/blog/app
 │   │   │   ├── config/             # 설정 파일 (보안, CORS 등)
 │   │   │   ├── auth/               # 인증 관련 (로그인, 회원가입)
 │   │   │   │   ├── controller/     # Auth 컨트롤러
 │   │   │   │   ├── dao/            # Auth DAO
 │   │   │   │   ├── mapper/         # Auth Mapper
 │   │   │   │   ├── service/        # Auth 서비스
 │   │   │   │   ├── vo/             # Auth VO (Value Object)
 │   │   │   ├── member/             # 회원 관련
 │   │   │   │   ├── controller/     # Member 컨트롤러
 │   │   │   │   ├── dao/            # Member DAO
 │   │   │   │   ├── mapper/         # Member Mapper
 │   │   │   │   ├── service/        # Member 서비스
 │   │   │   │   ├── vo/             # Member VO
 │   │   │   ├── post/               # 게시물 관련
 │   │   │   │   ├── controller/     # Post 컨트롤러
 │   │   │   │   ├── dao/            # Post DAO
 │   │   │   │   ├── mapper/         # Post Mapper
 │   │   │   │   ├── service/        # Post 서비스
 │   │   │   │   ├── vo/             # Post VO
 │   │   │   ├── comment/            # 댓글 관련
 │   │   │   │   ├── controller/     # Comment 컨트롤러
 │   │   │   │   ├── dao/            # Comment DAO
 │   │   │   │   ├── mapper/         # Comment Mapper
 │   │   │   │   ├── service/        # Comment 서비스
 │   │   │   │   ├── vo/             # Comment VO
 │   │   │   ├── image/              # 이미지 업로드 관련
 │   │   │   │   ├── controller/     # Image 컨트롤러
 │   │   │   │   ├── service/        # Image 서비스
 │   │   │   ├── BlogApplication.java # 메인 애플리케이션
 │   │   ├── resources
 │   │   │   ├── application.yml     # 환경설정 파일
 │   ├── test                        # 테스트 코드
 ├── pom.xml                         # Maven 설정 파일
 └── README.md                        # 프로젝트 설명
```

