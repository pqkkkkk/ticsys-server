package com.example.ticsys.order.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.example.ticsys.order.model.TicketOfOrder;

public class TicketOfOrderRowMapper implements RowMapper<TicketOfOrder> {
    
    @Override
    public TicketOfOrder mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return TicketOfOrder.builder()
                .id(rs.getInt("id"))
                .orderId(rs.getInt("orderId"))
                .ticketId(rs.getInt("ticketId"))
                .quantity(rs.getInt("quantity"))
                .build();
    }
}
