# Blog-BE-ItoR 이도연

# Blog 만들기
- 헥사곤 아키텍쳐에 대해 연습하고 있어서, 과제 또한 헥사곤 아키텍쳐로 구성 해보았습니다!
- 공통 로직(시큐리티, 응답객체, 예외핸들링)은 common으로 정했습니다.
- 도메인 관련 내용은 workspace로 정했습니다.

## ERD
![Leets](https://github.com/user-attachments/assets/f8443409-4a54-4bfd-8c44-20b0457c43d2)



## 프로젝트 구조
```
├── README.md
├── build.gradle
├── docker
│   └── docker-compose.yml
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── blog
    │   │           ├── BlogApplication.java
    │   │           ├── common
    │   │           │   ├── config
    │   │           │   │   └── JdbcConfig.java
    │   │           │   └── security
    │   │           │       ├── jwt
    │   │           │       │   ├── domain
    │   │           │       │   │   └── JwtToken.java
    │   │           │       │   ├── filter
    │   │           │       │   │   └── JwtAuthenticationFilter.java
    │   │           │       │   ├── handler
    │   │           │       │   │   └── JwtAccessDeniedHandler.java
    │   │           │       │   └── provider
    │   │           │       │       └── JwtTokenProvider.java
    │   │           │       └── oauth
    │   │           │           ├── controller
    │   │           │           │   └── OAuthController.java
    │   │           │           ├── domain
    │   │           │           │   └── OAuthUserInfo.java
    │   │           │           ├── handler
    │   │           │           │   ├── OAuth2AuthenticationFailureHandler.java
    │   │           │           │   └── OAuth2AuthenticationSuccessHandler.java
    │   │           │           └── service
    │   │           │               └── CustomOAuth2UserService.java
    │   │           └── workspace
    │   │               ├── adapter
    │   │               │   ├── in
    │   │               │   │   └── web
    │   │               │   │       ├── AuthController.java
    │   │               │   │       ├── CommentController.java
    │   │               │   │       ├── LoginController.java
    │   │               │   │       ├── PostController.java
    │   │               │   │       ├── UserController.java
    │   │               │   │       └── dto
    │   │               │   └── out
    │   │               │       ├── CommentPersistenceAdapter.java
    │   │               │       ├── PostPersistenceAdapter.java
    │   │               │       ├── UserPersistenceAdapter.java
    │   │               │       └── jdbc
    │   │               │           ├── BaseJdbc.java
    │   │               │           ├── comment
    │   │               │           │   ├── CommentJdbc.java
    │   │               │           │   └── CommentJdbcRepository.java
    │   │               │           ├── post
    │   │               │           │   ├── PostJdbc.java
    │   │               │           │   └── PostJdbcRepository.java
    │   │               │           └── user
    │   │               │               ├── UserJdbc.java
    │   │               │               └── UserJdbcRepository.java
    │   │               ├── application
    │   │               │   ├── in
    │   │               │   │   ├── AuthUseCase.java
    │   │               │   │   ├── CommentUseCase.java
    │   │               │   │   ├── LoginUseCase.java
    │   │               │   │   ├── PostUseCase.java
    │   │               │   │   └── UserUseCase.java
    │   │               │   ├── out
    │   │               │   │   ├── comment
    │   │               │   │   │   ├── DeleteCommentPort.java
    │   │               │   │   │   ├── LoadCommentPort.java
    │   │               │   │   │   └── SaveCommentPort.java
    │   │               │   │   ├── post
    │   │               │   │   │   ├── DeletePostPort.java
    │   │               │   │   │   ├── LoadPostPort.java
    │   │               │   │   │   └── SavePostPort.java
    │   │               │   │   └── user
    │   │               │   │       └── UserPort.java
    │   │               │   └── service
    │   │               │       ├── CommentService.java
    │   │               │       ├── LoginService.java
    │   │               │       ├── PostService.java
    │   │               │       └── UserService.java
    │   │               └── domain
    │   │                   ├── BaseDomain.java
    │   │                   ├── comment
    │   │                   │   └── Comment.java
    │   │                   ├── post
    │   │                   │   └── Post.java
    │   │                   └── user
    │   │                       ├── Social.java
    │   │                       └── User.java
    │   └── resources
    │       ├── application.yml
    │       └── sql
    │           └── schema.sql
    └── test
        └── java
            └── com
                └── blog
                    └── BlogApplicationTests.java
```

## 헥사곤 아키텍쳐
![image](https://github.com/user-attachments/assets/58b48da8-6da1-4075-b263-e2c3a51543e8)

- 사진 출처 : https://wikilog.tistory.com/entry/Today-I-Learned-24-240315-Hexagonal-Architecture를-바탕으로-한-기획을-하는-방법
