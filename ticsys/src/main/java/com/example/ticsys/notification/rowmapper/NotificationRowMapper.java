package com.example.ticsys.notification.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.example.ticsys.notification.model.Notification;

public class NotificationRowMapper implements RowMapper<Notification> {

    @Override
    public Notification mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return Notification.builder()
                .id(rs.getInt("id"))
                .receiverId(rs.getString("receiverId"))
                .eventId(rs.getInt("eventId"))
                .seen(rs.getBoolean("seen"))
                .template(rs.getString("template"))
                .type(rs.getString("type"))
                .build();
    }  

}
