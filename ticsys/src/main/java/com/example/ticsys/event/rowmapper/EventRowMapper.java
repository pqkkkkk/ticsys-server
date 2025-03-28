package com.example.ticsys.event.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.example.ticsys.event.model.Event;

public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event();
        event.setId(rs.getInt("id"));
        event.setOrganizerId(rs.getString("organizerId"));
        event.setName(rs.getString("name"));
        event.setLocation(rs.getString("location"));
        event.setDescription(rs.getString("description"));
        event.setBannerPath(rs.getString("bannerPath"));
        event.setSeatMapPath(rs.getString("seatMapPath"));
        event.setStatus(rs.getString("status"));
        event.setCategory(rs.getString("category"));
        event.setDate(rs.getDate("date").toLocalDate());
        event.setTime(rs.getTime("time"));
        return event;
    }
}
