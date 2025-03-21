# Blog-BE-ItoR 문석준

## ERD
![스크린샷 2025-03-21 오후 10.08.52.png](https://i.ibb.co/KcQgkVNv/2025-03-21-10-12-56.png)

## 디렉토리 구조
.
├── README.md
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
├── main
│   ├── java
│   │   └── com
│   │       └── blog
│   │           ├── BlogApplication.java
│   │           ├── domain
│   │           │   ├── comment
│   │           │   │   ├── controller
│   │           │   │   ├── domain
│   │           │   │   │   └── comment.java
│   │           │   │   ├── repository
│   │           │   │   └── service
│   │           │   ├── image
│   │           │   │   ├── controller
│   │           │   │   ├── domain
│   │           │   │   │   └── image.java
│   │           │   │   ├── repository
│   │           │   │   └── service
│   │           │   ├── post
│   │           │   │   ├── controller
│   │           │   │   ├── domain
│   │           │   │   │   └── post.java
│   │           │   │   ├── repository
│   │           │   │   └── service
│   │           │   └── user
│   │           │       ├── controller
│   │           │       ├── domain
│   │           │       │   └── user.java
│   │           │       ├── repository
│   │           │       └── service
│   │           └── global
│   │               ├── auth
│   │               ├── common
│   │               └── config
│   └── resources
│       └── application.properties
└── test
└── java
└── com
└── blog
└── BlogApplicationTests.java
