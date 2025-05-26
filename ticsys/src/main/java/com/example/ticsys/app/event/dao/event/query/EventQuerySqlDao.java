package com.example.ticsys.app.event.dao.event.query;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class EventQuerySqlDao implements IEventQueryDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public EventQuerySqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String GetUsernameOfEventOwner(Integer eventId) {
        String sql = "SELECT e.organizerId FROM [event] e WHERE e.ID = :eventId";
        Map<String, Object> params = Map.of("eventId", eventId);
        return jdbcTemplate.queryForObject(sql, params, String.class);
    }
}
