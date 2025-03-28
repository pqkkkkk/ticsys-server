package com.example.ticsys.order.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.example.ticsys.order.model.Order;

public class OrderRowMapper implements RowMapper<Order> {
    
    @Override
    @Nullable
    public Order mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setPrice(rs.getInt("price"));
        order.setCreatedBy(rs.getString("createdBy"));
        order.setEventId(rs.getInt("eventId"));
        order.setDateCreatedAt(rs.getDate("dateCreatedAt").toLocalDate());
        order.setTimeCreatedAt(rs.getTime("timeCreatedAt"));
        order.setStatus(rs.getString("status"));
        order.setPromotionId(rs.getInt("promotionId"));
        return order;
    }
}
