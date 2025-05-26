# Blog-BE-ItoR

## ERD
<img width="532" alt="스크린샷 2025-03-28 오전 10 24 35" src="https://github.com/user-attachments/assets/9ba1120e-9884-41f5-b128-8ec88113daff" />


## 프로젝트 구조 
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
    │   │               │   └── request
    │   │               │   └── response
    │   │               └── config
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── blog
                    └── BlogApplicationTests.java
```
