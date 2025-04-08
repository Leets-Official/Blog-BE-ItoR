# [Leets 5th] Blog 만들기

## ERD
<img width="893" alt="image" src="https://github.com/user-attachments/assets/c8ffcae8-7573-4a72-8b24-73d6197dc15a" />

## 디렉토리 구조

```
├── user/
│    ├── domain/
│    │    ├── User.java
│    │    ├── UserRepository.java
│    │    └── UserService.java
│    ├── application/
│    │    ├── UserServiceImpl.java
│    │    ├── LoginService.java
│    │    └── LoginServiceImpl.java
│    ├── infrastructure/
│    │    ├── UserRepositoryImpl.java
│    │    └── KakaoLoginServiceImpl.java
│    ├── presentation/
│    │    ├── UserController.java
│    │    ├── UserDto.java
│    │    └── LoginController.java
├── post/
│    ├── domain/
│    │    ├── Post.java
│    │    ├── PostRepository.java
│    │    └── PostService.java
│    ├── application/
│    │    ├── PostServiceImpl.java
│    └── infrastructure/
│         ├── PostRepositoryImpl.java
│    └── presentation/
│         ├── PostController.java
│         └── PostDto.java
├── comment/
│    ├── domain/
│    │    ├── Comment.java
│    │    ├── CommentRepository.java
│    │    └── CommentService.java
│    ├── application/
│    │    ├── CommentServiceImpl.java
│    └── infrastructure/
│         ├── CommentRepositoryImpl.java
│    └── presentation/
│         ├── CommentController.java
│         └── CommentDto.java
├── image/
│    ├── domain/
│    │    ├── Image.java
│    │    ├── ImageRepository.java
│    │    └── ImageService.java
│    ├── application/
│    │    ├── ImageServiceImpl.java
│    └── infrastructure/
│         ├── ImageRepositoryImpl.java
│    └── presentation/
│         ├── ImageController.java
│         └── ImageDto.java
├── resources/
│    ├── application.properties
│    └── db/
│         └── schema.sql
└── test/

```


## API 요구사항

### 회원가입(/auth)

- 사용자는 이메일 주소 또는 카카오 OAuth를 통해 회원가입을 진행할 수 있어야 합니다.
- 사용자는 비밀번호를 생성하여 회원가입을 진행할 수 있어야 합니다.
- 사용자는 프로필사진을 등록하며 회원가입을 진행할 수 있어야합니다(이메일 로그인에 한함)
- 사용자가 입력한 이메일 주소와 닉네임은 시스템에 이미 등록되어 있지 않아야 합니다.

### 로그인(/login)

- 사용자는 등록한 이메일 주소 또는 카카오 로그인을 이용하여 로그인할 수 있어야 합니다.
- (토큰 방식으로 구현시) refresh token을 통해 새로운 access token을 발급받을 수 있어야 합니다.

### 게시물 (/posts)

- 사용자는 로그인을 하지 않고도 게시물을 조회할 수 있어야 합니다.
- 사용자는 로그인을 진행해야 게시물을 작성할 수 있어야 합니다.
- 사용자는 자신의 게시물만 수정, 삭제할 수 있어야 합니다.
- 게시물은 페이지네이션이 가능해야 합니다.
  - 페이지네이션은 클라이언트에서 입력 받을 수 있게 구현해야 합니다.
- 게시물의 목록 조회와 게시물 내용을 보는 API는 별도로 구현되어야 합니다.
- 게시물 조회시 댓글도 모두 조회할 수 있어야 합니다.

### 댓글(/comments)

- 사용자는 로그인을 하지 않고도 댓글을 확인할 수 있어야 합니다.
- 사용자는 자신의 댓글만 수정, 삭제할 수 있어야 합니다.
- 댓글에는 댓글을 달수 없습니다(단 원하는 경우 구현해도 괜찮습니다)

### 유저(/users)

- 사용자는 닉네임, 비밀번호, 프로필 사진을 변경할 수 있어야 합니다.
- 사용자는 자신의 정보를 조회할 수 있어야 합니다.

### 이미지
- 이미지는 Pre-Signed Url 방식으로 업로드 할 수 있어야 합니다.

### 참고

- 모든 댓글, 게시물은 조회시 자신의 소유 여부를 응답으로 반환해야 합니다.
- 카카오 로그인과 이메일 로그인은 별도의 API로 구현되어야 합니다.

