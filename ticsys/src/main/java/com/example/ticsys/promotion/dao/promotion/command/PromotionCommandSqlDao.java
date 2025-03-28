package com.example.ticsys.promotion.dao.promotion.command;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.ticsys.promotion.model.Promotion;

@Repository
public class PromotionCommandSqlDao implements IPromotionCommandDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PromotionCommandSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
     @Override
    public int CreatePromotion(Promotion promotion) {
        String sql = """
                insert into [promotion] (eventId, minPriceToReach, promoPercent, voucherValue, status, type, startDate, endDate)
                values (:eventId, :minPriceToReach, :promoPercent, :voucherValue, :status, :type, :startDate, :endDate)
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("eventId", promotion.getEventId());
        params.put("minPriceToReach", promotion.getMinPriceToReach());
        params.put("promoPercent", promotion.getPromoPercent());
        params.put("voucherValue", promotion.getVoucherValue());
        params.put("status", promotion.getStatus());
        params.put("type", promotion.getType());
        params.put("startDate", promotion.getStartDate());
        params.put("endDate", promotion.getEndDate());
        
        SqlParameterSource parameterSource = new MapSqlParameterSource(params);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int updateCount = jdbcTemplate.update(sql, parameterSource, keyHolder, new String[] {"id"});

        if(updateCount > 0){
            Number key = keyHolder.getKey();
            if (key != null) {
                int eventId = key.intValue();
                return eventId;
            }
        }
        return -1;
    }

    @Override
    public int UpdatePromotion(Promotion promotion) {
        String sql = """
                update [promotion] set 
                minPriceToReach = :minPriceToReach, promoPercent = :promoPercent,
                voucherValue = :voucherValue, status = :status, type = :type, startDate = :startDate, endDate = :endDate
                where id = :id
                """;
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", promotion.getId());
        params.put("minPriceToReach", promotion.getMinPriceToReach());
        params.put("promoPercent", promotion.getPromoPercent());
        params.put("voucherValue", promotion.getVoucherValue());
        params.put("status", promotion.getStatus());
        params.put("type", promotion.getType());
        params.put("startDate", promotion.getStartDate());
        params.put("endDate", promotion.getEndDate());

        return jdbcTemplate.update(sql, params);
    }
}
