package com.example.ticsys.order.dao.order.command;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.ticsys.order.model.Order;

@Repository
public class OrderCommandSqlDao implements IOrderCommandDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public OrderCommandSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Integer CreateOrder(Order order) {
        String sql = """ 
            INSERT INTO [order] (price, createdBy, eventId, dateCreatedAt, timeCreatedAt, status, promotionId)
            VALUES (:price, :createdBy, :eventId, :dateCreatedAt, :timeCreatedAt, :status, :promotionId)
        """;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("price", order.getPrice());
        paramMap.put("createdBy", order.getCreatedBy());
        paramMap.put("eventId", order.getEventId());
        paramMap.put("dateCreatedAt", order.getDateCreatedAt());
        paramMap.put("timeCreatedAt", order.getTimeCreatedAt());
        paramMap.put("status", order.getStatus());
        paramMap.put("promotionId", order.getPromotionId() == -1 ? null : order.getPromotionId());

        SqlParameterSource parameterSource = new MapSqlParameterSource(paramMap);
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
    public Integer UpdateOrder(int id, Map<String, Object> orderValues) {
        if (orderValues.isEmpty()) {
            throw new IllegalArgumentException("orderValues cannot be empty");
        }
          
        StringBuilder sqlBuilder = new StringBuilder("UPDATE [order] SET ");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        int count = 0;
        for (Map.Entry<String, Object> entry : orderValues.entrySet()) {
            if (count > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append(entry.getKey()).append(" = :").append(entry.getKey());
            paramMap.put(entry.getKey(), entry.getValue());
            count++;
        }
        sqlBuilder.append(" WHERE id = :id");
        
        String sql = sqlBuilder.toString();
        return jdbcTemplate.update(sql, paramMap);
    }

}
