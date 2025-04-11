# Blog-BE-ItoR 이도연

# Blog 만들기
- 헥사곤 아키텍쳐에 대해 연습하고 있어서, 과제 또한 헥사곤 아키텍쳐로 구성 해보았습니다!
- 공통 로직(시큐리티, 응답객체, 예외핸들링)은 common으로 정했습니다.
- 도메인 관련 내용은 workspace로 정했습니다.

## ERD
![Leets](https://github.com/user-attachments/assets/95c1da3c-3fe7-4516-8687-3b5dbb0185dd)


## 프로젝트 구조
```
src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── blog
    │   │           ├── BlogApplication.java
    │   │           ├── common
    │   │           │   ├── config
    │   │           │   ├── exception
    │   │           │   ├── response
    │   │           │   │   └── page
    │   │           │   └── util
    │   │           ├── security
    │   │           │   └── jwt
    │   │           └── workspace
    │   │               ├── adapter
    │   │               │   ├── in
    │   │               │   │   └── web
    │   │               │   │       └── dto
    │   │               │   │           ├── request
    │   │               │   │           └── response
    │   │               │   └── out
    │   │               │       ├── jdbc
    │   │               │       └── oauth
    │   │               ├── application
    │   │               │   ├── in
    │   │               │   ├── out
    │   │               │   └── service
    │   │               │       └── exception
    │   │               └── domain
    │   └── resources
    │       ├── application.yml
    │       └── sql
    │           └── schema.sql
```

## 헥사곤 아키텍쳐
![image](https://github.com/user-attachments/assets/58b48da8-6da1-4075-b263-e2c3a51543e8)

- 사진 출처 : https://wikilog.tistory.com/entry/Today-I-Learned-24-240315-Hexagonal-Architecture를-바탕으로-한-기획을-하는-방법
