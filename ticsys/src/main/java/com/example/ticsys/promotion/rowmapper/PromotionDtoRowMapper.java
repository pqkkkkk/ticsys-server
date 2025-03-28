package com.example.ticsys.promotion.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.example.ticsys.promotion.dto.PromotionDto;

public class PromotionDtoRowMapper implements RowMapper<PromotionDto> {

    @Override
    public PromotionDto mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int reduction = rs.getInt("reduction");
        if (rs.wasNull()) {
            reduction = 0;
        }
        int orderCount = rs.getInt("orderCount");
        if (rs.wasNull()) {
            orderCount = 0;
        }
        PromotionDto promotion = PromotionDto.builder()
            .id(rs.getInt("id"))
            .eventId(rs.getInt("eventId"))
            .startDate(rs.getDate("startDate").toLocalDate())
            .endDate(rs.getDate("endDate").toLocalDate())
            .reduction(reduction)
            .orderCount(orderCount)
            .minPriceToReach(rs.getInt("minPriceToReach"))
            .promoPercent(rs.getInt("promoPercent"))
            .voucherValue(rs.getInt("voucherValue"))
            .status(rs.getString("status"))
            .type(rs.getString("type"))
            .build();
        return promotion;
    }

}
