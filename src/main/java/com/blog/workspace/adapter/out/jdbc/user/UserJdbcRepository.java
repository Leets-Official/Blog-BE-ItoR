package com.blog.workspace.adapter.out.jdbc.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /// 저장
    public UserJdbc save(UserJdbc userJdbc) {
        String sql = "INSERT INTO User (email, username, nickname, password, image_url, social, description, birthday,  created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                userJdbc.getEmail(),
                userJdbc.getUsername(),
                userJdbc.getNickname(),
                userJdbc.getPassword(),
                userJdbc.getImageUrl(),
                userJdbc.isSocial(),
                userJdbc.getDescription(),
                userJdbc.getBirthday(),
                userJdbc.getCreatedAt(),
                userJdbc.getUpdatedAt());

        return userJdbc;
    }

    /// 조회
    public Optional<UserJdbc> findById(Long id) {
        String sql = "SELECT * FROM User WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper(), id)
                .stream()
                .findFirst();
    }

    /// 존재 여부 파악
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM User WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public Optional<UserJdbc> findByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        return jdbcTemplate.query(sql, userRowMapper(), email)
                .stream()
                .findFirst();
    }

    public UserJdbc updateUser(UserJdbc userJdbc) {

        String sql = "UPDATE User SET " +
                "email = ?, username = ?, nickname = ?, password = ?, image_url = ?, social = ?, description = ?, birthday = ?, updated_at = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sql,
                userJdbc.getEmail(),
                userJdbc.getUsername(),
                userJdbc.getNickname(),
                userJdbc.getPassword(),
                userJdbc.getImageUrl(),
                userJdbc.isSocial(),
                userJdbc.getDescription(),
                userJdbc.getBirthday(),
                userJdbc.getUpdatedAt(),
                userJdbc.getId());

        return userJdbc;
    }


    private RowMapper<UserJdbc> userRowMapper() {
        return (rs, rowNum) -> {
            return new UserJdbc(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("nickname"),
                    rs.getString("password"),
                    rs.getString("image_url"),
                    rs.getBoolean("social"),
                    rs.getString("description"),
                    rs.getString("birthday"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );
        };
    }



}

