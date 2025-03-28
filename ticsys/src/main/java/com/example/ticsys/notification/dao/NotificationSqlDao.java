package com.example.ticsys.notification.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ticsys.notification.model.Notification;
import com.example.ticsys.notification.rowmapper.NotificationRowMapper;

@Repository
public class NotificationSqlDao implements INotificationDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public NotificationSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public boolean CreateNotification(Notification notification) {
        String sql = """
                INSERT INTO [notification] (receiverId, eventId, seen, type)
                VALUES (:receiverId, :eventId, :seen, :type)
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("receiverId", notification.getReceiverId());
        params.put("eventId", notification.getEventId());
        params.put("seen", notification.isSeen());
        params.put("type", notification.getType());

        return jdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public boolean UpdateNotification(Notification notification) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'UpdateNotification'");
    }

    @Override
    public List<Notification> GetNotifications(String receiverId, int eventId, boolean seen, String type) {
        String sql = "SELECT * FROM [notification] WHERE 1=1 ";
        Map<String, Object> params = new HashMap<>();

        if (receiverId != null) {
            sql += "AND receiverId = :receiverId ";
            params.put("receiverId", receiverId);
        }
        if (eventId != 0) {
            sql += "AND eventId = :eventId ";
            params.put("eventId", eventId);
        }
        if (seen) {
            sql += "AND seen = :seen ";
            params.put("seen", seen);
        }
        if (type != null) {
            sql += "AND type = :type ";
            params.put("type", type);
        }

        return jdbcTemplate.query(sql, params, new NotificationRowMapper());

    }

}
