CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 유저 PK, 자동 증가
  email VARCHAR(128) NOT NULL,                 -- 이메일, NULL 불가
  password VARCHAR(512) NOT NULL,              -- 비밀번호, NULL 불가
  name VARCHAR(20) NOT NULL,                   -- 이름, NULL 불가
  nickname VARCHAR(20) NOT NULL,               -- 닉네임, NULL 불가
  birth DATE NOT NULL,                         -- 생년월일, NULL 불가
  profile_image VARCHAR(512) DEFAULT NULL,     -- 프로필 이미지 (이메일 가입 시), NULL 허용
  social BOOLEAN NOT NULL,                  -- 가입 경로, NULL 불가
  introduce VARCHAR(30) NOT NULL,              -- 한줄소개, NULL 불가
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일, 기본값 현재 시간
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP-- 수정일, 수정 시 자동 업데이트
);

CREATE TABLE posts (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 게시글 고유 ID (PK, 자동 증가)
  user_id INT NOT NULL,                        -- 글쓴이 번호 (users 테이블의 PK)
  subject VARCHAR(256) NOT NULL,               -- 게시글 제목
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 생성일
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 수정일

);

CREATE TABLE post_blocks (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 게시글 단락 고유 ID (PK, 자동 증가)
  post_id INT NOT NULL,                        -- 게시글 ID (posts 테이블의 PK)
  content TEXT NOT NULL,                       -- 게시글 단락 내용
  image_url VARCHAR(512) NOT NULL             -- 게시글 단락의 이미지 URL
);

CREATE TABLE comments (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 댓글 고유 ID (PK, 자동 증가)
  post_id INT NOT NULL,                        -- 게시글 번호 (posts 테이블의 PK)
  user_id INT NOT NULL,                        -- 댓글을 작성한 사용자 (users 테이블의 PK)
  content TEXT NOT NULL,                       -- 댓글 내용
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 생성일
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- 수정일
);