package com.example.stylish.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.stylish.dto.UserDto;

import jakarta.annotation.Nullable;

@Repository
public class UserRepository {


    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public boolean facebookIdExists(String facebookId) {
        String sql = "SELECT COUNT(*) FROM user WHERE facebook_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, facebookId);
        return count != null && count > 0;
    }


    public int saveUser(String provider, String name, String email, String password, String picture, String facebookId) {
        String sql = "INSERT INTO user (provider, name, email, password, picture, facebook_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] {"user_id"});
                ps.setString(1, provider);
                ps.setString(2, name);
                ps.setString(3, email);
                ps.setString(4, password);
                ps.setString(5, picture);
                ps.setString(6, facebookId);
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public String findPasswordByEmail(String email) {
        String sql = "SELECT password FROM user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, String.class, email);
    }

    public UserDto findUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            UserDto user = new UserDto();
            user.setId(rs.getInt("user_id"));
            user.setProvider(rs.getString("provider"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPicture(rs.getString("picture"));
            return user;
        }, email);
    }

    public UserDto findUserByFacebookId(String facebookId) {
        String sql = "SELECT * FROM user WHERE facebook_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            UserDto user = new UserDto();
            user.setId(rs.getInt("user_id"));
            user.setProvider(rs.getString("provider"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPicture(rs.getString("picture"));
            return user;
        }, facebookId);
    }

    public void updateUser(String name, String picture, String email,  int userId) {
    String sql = "UPDATE user SET name = ?, picture = ?, email = ? WHERE user_id = ?";
    jdbcTemplate.update(sql, name, picture, email, userId);
    }

    @Nullable
    public Integer findRoleIdByRoleName(String roleName) {
        String query = "SELECT role_id FROM role WHERE role_name = ?";
        try {
            return jdbcTemplate.queryForObject(query, Integer.class, roleName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int saveRole(String roleName) {
        String query = "INSERT INTO role (role_name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(query, new String[] {"role_id"});
                ps.setString(1, roleName);
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void saveUserRole(int userId, String roleName){

        Integer roleId = findRoleIdByRoleName(roleName);

        if(roleId == null){
            roleId = saveRole(roleName);
        }

        String query = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        jdbcTemplate.update(query, userId, roleId);
    }

    public List<String> findRoleByUserId(int userId) {
        String sql = "SELECT r.role_name FROM role r " +
                        "JOIN user_role ur ON r.role_id = ur.role_id " +
                        "WHERE ur.user_id = ?";

        return jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("role_name");
            }
        },userId);
    }
}



