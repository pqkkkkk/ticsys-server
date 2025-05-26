package com.example.ticsys.app.order.dao.order.query;

import java.sql.Time;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ticsys.app.order.dto.OrderDto;
import com.example.ticsys.app.order.dto.OrderInfoForCheckAuthorityDto;
import com.example.ticsys.app.order.dto.OrderWithTicketCountDto;
import com.example.ticsys.app.order.mapper.OrderDtoExtractor;
import com.example.ticsys.app.order.mapper.OrderInfoForCARowMapper;
import com.example.ticsys.app.order.mapper.OrderRowMapper;
import com.example.ticsys.app.order.mapper.OrderWithTCDtoRowMapper;
import com.example.ticsys.app.order.model.Order;

@Repository
public class OrderQuerySqlDao implements IOrderQueryDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public OrderQuerySqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public OrderInfoForCheckAuthorityDto GetOrderInfoForCheckAuthority(int orderId) {
        String sql = """
                    SELECT o.id, o.createdBy, o.eventId, e.organizerId as eventOwner FROM [order] o
                    JOIN event e ON o.eventId = e.id
                    WHERE o.id = :id        
                """;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", orderId);

        return jdbcTemplate.queryForObject(sql, paramMap, new OrderInfoForCARowMapper());
    }
    @Override
    public Order GetOrderById(int id) {
         String sql ="""
            SELECT * FROM [order] WHERE id = :id
        """;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        return jdbcTemplate.queryForObject(sql, paramMap, new OrderRowMapper());
    }
    @Override
    public OrderDto GetOrderWithAllRelatedInfoById(int orderId) {
        String sql = """
        select o.*, u.userName, u.fullName, u.email, u.phoneNumber,
        e.organizerId as eventOwner, e.name as eventName,
        e.description as eventDescription, e.location as eventLocation, e.date as eventDate,
        e.bannerPath as eventBannerPath, e.status as eventStatus, e.category as eventCategory, e.time as eventTime,
        too.id as ticketOfOrderId, too.quantity as  ticketOfOrderQuantity
        t.id as ticketId, t.name as ticketName, t.price as ticketPrice, t.service as ticketService
        from [Order] o join Users u on o.createdBy = u.userName
				join TicketOfOrder too on o.ID = too.orderId
				join Event e on o.eventId = e.ID
				join Ticket t on e.id = t.eventId and too.ticketId = t.ID
        where o.ID = :orderId
                """;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId);

        return jdbcTemplate.query(sql, paramMap, new OrderDtoExtractor());
    }
    @Override
    public List<Order> GetOrders(String userId, LocalDate dateCreated, Time timeCreated, String status, Integer eventId) {
        String sql = "SELECT * FROM [order] WHERE 1=1 ";
        Map<String, Object> paramMap = new HashMap<>();
        
        if(eventId != -1){
            sql += "AND eventId = :eventId ";
            paramMap.put("eventId", eventId);
        }
        if(userId != null){
            sql += "AND createdBy = :userId ";
            paramMap.put("userId", userId);
        }
        if(dateCreated != null){
            sql += "AND dateCreatedAt = :dateCreated ";
            paramMap.put("dateCreated", dateCreated);
        }
        if(timeCreated != null){
            sql += "AND timeCreatedAt = :timeCreated ";
            paramMap.put("timeCreated", timeCreated);
        }
        if(status != null){
            sql += "AND status = :status ";
            paramMap.put("status", status);
        }
        
        return jdbcTemplate.query(sql, paramMap, new OrderRowMapper());
    }
    @Override
    public List<Order> SearchOrders(String userFullNameKeyword, int eventId) {
        String sql = """
            SELECT * FROM [order] o  JOIN users u ON o.createdBy = u.userName 
            WHERE u.fullname LIKE :userFullNameKeyword
                """;
                
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userFullNameKeyword", "%" + userFullNameKeyword + "%");

        if(eventId != -1){
            sql += " AND eventId = :eventId ";
            paramMap.put("eventId", eventId);
        }
        
        return jdbcTemplate.query(sql, paramMap, new OrderRowMapper());
    }
    @Override
    public List<OrderWithTicketCountDto> GetOrders(String userId, LocalDate dateCreated, Time timeCreated,
            String status, Integer eventId, Integer ticketTypeId) {

        StringBuilder sql = new StringBuilder("SELECT o.*, SUM(too.quantity) as ticketCount FROM [order] o ");
        sql.append(" JOIN TicketOfOrder too ON o.ID = too.orderId ");
        sql.append(" WHERE 1=1 ");

        Map<String, Object> paramMap = new HashMap<>();
        
        if(eventId != -1){
            sql.append(" AND eventId = :eventId ");
            paramMap.put("eventId", eventId);
        }

        if(ticketTypeId != -1){
            sql.append(" AND too.ticketId = :ticketTypeId ");
            paramMap.put("ticketTypeId", ticketTypeId);
        }
        
        sql.append(" GROUP BY o.ID, o.createdBy, o.DateCreatedAt, o.eventId, o.price, o.promotionId, o.status, o.TimeCreatedAt");
        return jdbcTemplate.query(sql.toString(), paramMap, new OrderWithTCDtoRowMapper());


    }

}
