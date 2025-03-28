package com.example.ticsys.promotion.dao.promotion.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ticsys.promotion.dto.PromotionDto;
import com.example.ticsys.promotion.rowmapper.PromotionDtoRowMapper;

@Repository
public class PromotionQuerySqlDao implements IPromotionQueryDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PromotionQuerySqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
     @Override
    public List<PromotionDto> GetPromotions(int eventId, String status, String type) {
        String sql = "SELECT *, null as reduction, null as orderCount FROM [promotion] WHERE 1=1 ";
        Map<String, Object> paramMap = new HashMap<>();

        if(eventId != -1){
            sql += "AND eventId = :eventId ";
            paramMap.put("eventId", eventId);
        }
        if(status != null){
            sql += "AND status = :status ";
            paramMap.put("status", status);
        }
        if(type != null){
            sql += "AND type = :type ";
            paramMap.put("type", type);
        }
        return jdbcTemplate.query(sql, paramMap, new PromotionDtoRowMapper());
    }

    @Override
    public PromotionDto GetPromotionById(int promotionId) {
        String sql = "SELECT *, null as reduction, null as orderCount FROM [promotion] WHERE id = :id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", promotionId);
        return jdbcTemplate.queryForObject(sql, paramMap, new PromotionDtoRowMapper());
    }
    @Override
    public List<PromotionDto> GetPromotionsWithOrderCount(int eventId, String status, String type) {
        StringBuilder sql = new StringBuilder("""
            select p.*, count(o.id) as orderCount, null as reduction
            from Promotion p join [Order] o on p.ID = o.promotionId
            where 1=1 
        """);

        Map<String, Object> paramMap = new HashMap<>();

        if(eventId != -1){
            sql.append("AND p.eventId = :eventId ");
            paramMap.put("eventId", eventId);
        }
        if(status != null){
            sql.append("AND p.status = :status ");
            paramMap.put("status", status);
        }
        if(type != null){
            sql.append("AND p.type = :type ");
            paramMap.put("type", type);
        }

        sql.append(" group by p.ID,p.eventId,p.endDate,p.MinPriceToReach,p.promoPercent,p.startDate,p.status,p.type,p.voucherValue");
        return jdbcTemplate.query(sql.toString(), paramMap, new PromotionDtoRowMapper());
    }
}
