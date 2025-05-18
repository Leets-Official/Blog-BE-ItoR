CREATE TABLE `user` (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        name VARCHAR(50) NOT NULL UNIQUE,
                        nickname VARCHAR(50) NOT NULL,
                        introduction VARCHAR(255) NOT NULL,
                        birth DATE NOT NULL,
                        profile_image VARCHAR(255),
                        provider ENUM('email', 'kakao') DEFAULT 'email' NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `post` (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        comment_count INT DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE `comment` (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           user_id BIGINT NOT NULL,
                           post_id BIGINT NOT NULL,
                           content TEXT NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE,
                           FOREIGN KEY (post_id) REFERENCES `post`(id) ON DELETE CASCADE
);

CREATE TABLE post_content (

                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              post_id BIGINT NOT NULL,
                              type VARCHAR(20) NOT NULL, -- TEXT or IMAGE
                              content TEXT NOT NULL,
                              sequence INT NOT NULL,
                              FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
);
