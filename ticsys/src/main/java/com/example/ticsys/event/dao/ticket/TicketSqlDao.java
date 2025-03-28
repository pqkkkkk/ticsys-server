package com.example.ticsys.event.dao.ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ticsys.event.model.Ticket;
import com.example.ticsys.event.rowmapper.TicketRowMapper;

@Repository
public class TicketSqlDao implements ITicketDao{
    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    public TicketSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public boolean AddTicket(Ticket ticket) {
        String sql = """
            INSERT INTO ticket (eventId, price, quantity, service, name, minQtyInOrder, maxQtyInOrder)
            VALUES (:eventId, :price, :quantity, :service, :name, :minQtyInOrder, :maxQtyInOrder)
                """;
        
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("eventId", ticket.getEventId());
        paramMap.put("price", ticket.getPrice());
        paramMap.put("quantity", ticket.getQuantity());
        paramMap.put("service", ticket.getService());
        paramMap.put("name", ticket.getName());
        paramMap.put("minQtyInOrder", ticket.getMinQtyInOrder());
        paramMap.put("maxQtyInOrder", ticket.getMaxQtyInOrder());

        return jdbcTemplate.update(sql, paramMap) > 0;
    }

    @Override
    public List<Ticket> GetTicketsOfEvent(int eventId) {
        String sql = """
            SELECT * FROM ticket WHERE eventId = :eventId
                """;
        
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("eventId", eventId);

        return jdbcTemplate.query(sql,paramMap, new TicketRowMapper());
    }
    @Override
    public Map<String, Object> GetTicketByRequiredFieldsList(List<String> requiredFields, int id) {
        String fieldStr = String.join(", ", requiredFields);
        String sql = "SELECT " + fieldStr + " FROM ticket WHERE id = :id";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        return jdbcTemplate.queryForMap(sql, paramMap);
    }
    @Override
    public int UpdateTicketByRequiredFieldsList(Map<String, Object> newValues, int id) {
        if(newValues.isEmpty()){
            return -1;
        }

        String sql = "UPDATE ticket SET ";
        for (Map.Entry<String, Object> entry : newValues.entrySet()) {
            sql += entry.getKey() + " = :" + entry.getKey() + ", ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += " WHERE id = :id";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.putAll(newValues);

        int result =  jdbcTemplate.update(sql, paramMap);
        return result;
    }
    @Override
    public Ticket GetTicketById(int id) {
        String sql = "SELECT * FROM ticket WHERE id = :id";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        return jdbcTemplate.queryForObject(sql, paramMap, new TicketRowMapper());
    }

}
