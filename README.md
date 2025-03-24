# Blog-BE-ItoR

# ERD
<img width="543" alt="스크린샷 2025-03-24 오후 11 56 06" src="https://github.com/user-attachments/assets/6fda8801-26ee-4d3c-9d7c-c65400c0c688" />

# 프로젝트 구조
```
├── README.md
├── build.gradle
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── blog
    │   │           ├── BlogApplication.java
    │   │           ├── domain
    │   │           │   ├── comment
    │   │           │   │   └── controller
    │   │           │   │   └── domain
    │   │           │   │   └── repository
    │   │           │   │   └── service
    │   │           │   ├── image
    │   │           │   │   └── controller
    │   │           │   │   └── domain
    │   │           │   │   └── repository
    │   │           │   │   └── service
    │   │           │   ├── post
    │   │           │   │   └── controller
    │   │           │   │   └── domain
    │   │           │   │   └── repository
    │   │           │   │   └── service
    │   │           │   └── user
    │   │           │   │   └── controller
    │   │           │   │   └── domain
    │   │           │   │   └── repository
    │   │           │   │   └── service
    │   │           └── global
    │   │               ├── auth
    │   │               ├── common
    │   │               │   └── request
    │   │               │   └── response
    │   │               ├── config
    │   │               └── exception
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── blog
                    └── BlogApplicationTests.java
```
