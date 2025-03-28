package com.example.ticsys.order.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.example.ticsys.order.dto.OrderInfoForCheckAuthorityDto;

public class OrderInfoForCARowMapper implements RowMapper<OrderInfoForCheckAuthorityDto> {

    @Override
    public OrderInfoForCheckAuthorityDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return OrderInfoForCheckAuthorityDto.builder()
                .orderId(rs.getInt("id"))
                .createdBy(rs.getString("createdBy"))
                .eventId(rs.getInt("eventId"))
                .eventOwner(rs.getString("eventOwner"))
                .build();
    }

}
