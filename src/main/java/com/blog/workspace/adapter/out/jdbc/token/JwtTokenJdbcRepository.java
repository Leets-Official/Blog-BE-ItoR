package com.blog.workspace.adapter.out.jdbc.token;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JwtTokenJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public JwtTokenJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 저장
    // 저장 또는 업데이트
    public JwtTokenJdbc upsert(JwtTokenJdbc jwtTokenJdbc) {
        // 먼저 존재하는 토큰을 찾는다
        String checkSql = "SELECT COUNT(*) FROM Token WHERE refresh_token = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, jwtTokenJdbc.getRefreshToken());

        if (count != null && count > 0) {
            // 존재하면 업데이트
            String updateSql = "UPDATE Token SET refresh_token = ?, expired_at = ? WHERE refresh_token = ?";
            jdbcTemplate.update(updateSql, jwtTokenJdbc.getRefreshToken(), jwtTokenJdbc.getExpiredAt(), jwtTokenJdbc.getRefreshToken());
        } else {
            // 존재하지 않으면 새로 추가
            String insertSql = "INSERT INTO Token (user_id, refresh_token, expired_at) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, jwtTokenJdbc.getUserId(), jwtTokenJdbc.getRefreshToken(), jwtTokenJdbc.getExpiredAt());
        }

        return jwtTokenJdbc;
    }

    // 토큰 삭제
    public void deleteByRefreshToken(String refreshToken) {
        String sql = "DELETE FROM Token WHERE refresh_token = ?";
        jdbcTemplate.update(sql, refreshToken);
    }

    // 존재 여부 확인: 사용자 ID로 토큰 존재 여부 확인
    public boolean existsByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM Token WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    // 토큰을 바탕으로 사용자 ID 가져오기
    public Optional<Long> findUserIdByRefreshToken(String refreshToken) {
        String sql = "SELECT user_id FROM Token WHERE refresh_token = ? AND expired_at > now()";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Optional.of(rs.getLong("user_id")), refreshToken);
    }

    // RowMapper 정의
    private RowMapper<JwtTokenJdbc> tokenRowMapper() {
        return (rs, rowNum) -> new JwtTokenJdbc(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("refresh_token"),
                rs.getTimestamp("expired_at").toLocalDateTime()
        );
    }
}
