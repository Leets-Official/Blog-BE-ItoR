ALTER TABLE user ADD CONSTRAINT chk_email_format CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');
ALTER TABLE user ADD CONSTRAINT chk_name_length CHECK (CHAR_LENGTH(name) <= 10);
ALTER TABLE user ADD CONSTRAINT chk_nickname_length CHECK (CHAR_LENGTH(nickname) <= 20);
ALTER TABLE user ADD CONSTRAINT chk_introduction_length CHECK (CHAR_LENGTH(introduction) <= 30);

ALTER TABLE user MODIFY COLUMN provider VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
