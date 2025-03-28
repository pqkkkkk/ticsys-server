package com.example.ticsys.promotion.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.example.ticsys.promotion.model.Promotion;

public class PromotionRowMapper implements RowMapper<Promotion> {

    @Override
    public Promotion mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Promotion promotion = new Promotion();
        promotion.setId(rs.getInt("id"));
        promotion.setEventId(rs.getInt("eventId"));
        promotion.setMinPriceToReach(rs.getInt("minPriceToReach"));
        promotion.setPromoPercent(rs.getInt("promoPercent"));
        promotion.setVoucherValue(rs.getInt("voucherValue"));
        promotion.setStatus(rs.getString("status"));
        promotion.setType(rs.getString("type"));
        promotion.setStartDate(rs.getDate("startDate").toLocalDate());
        promotion.setEndDate(rs.getDate("endDate").toLocalDate());
        return promotion;
    }

}
