package com.example.ticsys.app.order.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.example.ticsys.app.order.dto.OrderWithTicketCountDto;

public class OrderWithTCDtoRowMapper implements RowMapper<OrderWithTicketCountDto> {

    @Override
    public OrderWithTicketCountDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return OrderWithTicketCountDto.builder()
                .id(rs.getInt("ID"))
                .createdBy(rs.getString("createdBy"))
                .dateCreatedAt(rs.getDate("dateCreatedAt").toLocalDate())
                .timeCreatedAt(rs.getTime("timeCreatedAt"))
                .status(rs.getString("status"))
                .eventId(rs.getInt("eventId"))
                .ticketCount(rs.getInt("ticketCount"))
                .promotionId(rs.getInt("promotionId"))
                .price(rs.getInt("price"))
                .build();
    }

}
