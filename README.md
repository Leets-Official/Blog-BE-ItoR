# Blog-BE-ItoR 문석준

## ERD
![ERD Diagram](https://i.ibb.co/1fcbTngb/2025-03-25-3-11-55.png)


## 디렉토리 구조

```plaintext
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
    │   │           │   │   │   └── Comment.java
    │   │           │   │   ├── repository
    │   │           │   │   └── service
    │   │           │   ├── post
    │   │           │   │   ├── controller
    │   │           │   │   ├── domain
    │   │           │   │   │   ├── Post.java
    │   │           │   │   │   └── PostImage.java
    │   │           │   │   ├── repository
    │   │           │   │   └── service
    │   │           │   └── user
    │   │           │       ├── controller
    │   │           │       ├── domain
    │   │           │       │   └── User.java
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


