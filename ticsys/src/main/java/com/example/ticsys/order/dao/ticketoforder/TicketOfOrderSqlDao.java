package com.example.ticsys.order.dao.ticketoforder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ticsys.order.mapper.TicketOfOrderRowMapper;
import com.example.ticsys.order.model.TicketOfOrder;

@Repository
public class TicketOfOrderSqlDao implements ITicketOfOrderDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TicketOfOrderSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public boolean CreateTicketOfOrder(TicketOfOrder ticketOfOrder) {
        String sql = """
            INSERT INTO ticketOfOrder (orderId, ticketId, quantity)
            VALUES (:orderId, :ticketId, :quantity)
            """;
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", ticketOfOrder.getOrderId());
        params.put("ticketId", ticketOfOrder.getTicketId());
        params.put("quantity", ticketOfOrder.getQuantity());

        return jdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public List<TicketOfOrder> GetTicketsOfOrder(int orderId) {
        String sql = """
            SELECT * FROM ticketOfOrder
            WHERE orderId = :orderId
            """;
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);

        return jdbcTemplate.query(sql, params, new TicketOfOrderRowMapper());
    }

}
