# Blog-BE-ItoR

## ERD (https://dbdiagram.io/d/Leets-Blog-67dd20bf75d75cc844f14439)
![img.png](img.png)

## 프로젝트 구조
```
src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─blog
    │  │          ├─domain
    │  │          │  ├─comment
    │  │          │  │  ├─controller
    │  │          │  │  ├─domain
    │  │          │  │  ├─repository
    │  │          │  │  └─service
    │  │          │  ├─image
    │  │          │  │  ├─controller
    │  │          │  │  ├─domain
    │  │          │  │  ├─repository
    │  │          │  │  └─service
    │  │          │  ├─post
    │  │          │  │  ├─controller
    │  │          │  │  ├─domain
    │  │          │  │  ├─repository
    │  │          │  │  └─service
    │  │          │  └─user
    │  │          │      ├─controller
    │  │          │      ├─domain
    │  │          │      ├─repository
    │  │          │      └─service
    │  │          └─global
    │  │              ├─common
    │  │              ├─config
    │  │              ├─response
    │  │              └─security
    │  └─resources
    │      └─sql
    └─test
        └─java
            └─com
                └─blog
```