package com.example.ticsys.account.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.example.ticsys.account.model.User;


public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException{
        User user = new User();
        user.setUserName(rs.getString("username"));
        user.setPassWord(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("fullName"));
        user.setPhoneNumber(rs.getString("phoneNumber"));
        user.setBirthday(rs.getDate("birthday"));
        user.setGender(rs.getString("gender"));
        user.setAvatarPath(rs.getString("avatarPath"));
        return user;
    }
}
