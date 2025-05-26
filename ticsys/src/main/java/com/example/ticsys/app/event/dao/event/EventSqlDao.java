package com.example.ticsys.app.event.dao.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.ticsys.app.event.model.Event;
import com.example.ticsys.app.event.rowmapper.EventRowMapper;

@Repository
public class EventSqlDao implements IEventDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    public EventSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public int CreateEvent(Event event) {
        String sql = """
            INSERT INTO event (name,organizerId, location, description, bannerPath, seatMapPath, status, category, date, time)
            VALUES (:name,:organizerId, :location, :description, :bannerPath, :seatMapPath, :status, :category, :date, :time)
            """;
        Map<String, Object> params = new HashMap<>();
        params.put("name", event.getName());
        params.put("organizerId", event.getOrganizerId());
        params.put("location", event.getLocation());
        params.put("description", event.getDescription());
        params.put("bannerPath", event.getBannerPath());
        params.put("seatMapPath", event.getSeatMapPath());
        params.put("status", event.getStatus());
        params.put("category", event.getCategory());
        params.put("date", event.getDate());
        params.put("time", event.getTime());

        SqlParameterSource parameterSource = new MapSqlParameterSource(params);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int updateCount =  jdbcTemplate.update(sql, parameterSource, keyHolder, new String[] {"id"});

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
    public Event GetEventById(int id) {
        String sql = "SELECT * FROM event WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.queryForObject(sql, params, new EventRowMapper());
    }
    @Override
    public List<Event> GetEvents(String category, String status, String organizerId) {
        String sql = "SELECT * FROM event WHERE 1=1 ";
        Map<String, Object> params = new HashMap<>();
        if (category != null) {
            sql += "AND category = :category ";
            params.put("category", category);
        }
        if (status != null) {
            sql += "AND status = :status ";
            params.put("status", status);
        }
        if (organizerId != null) {
            sql += "AND organizerId = :organizerId ";
            params.put("organizerId", organizerId);
        }
        
        return jdbcTemplate.query(sql, params, new EventRowMapper());
    }
    @Override
    public Map<String, Object> GetEventByRequiredFieldsList(List<String> requiredFields, int id) {
        
        String fieldStr = String.join(", ", requiredFields);
        String sql = "SELECT " + fieldStr + " FROM [event] WHERE id = :id";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        return jdbcTemplate.queryForMap(sql, paramMap);
    }
    @Override
    public List<Event> GetEvents(Map<String, Object> filterMap) {
        String sql = "SELECT * FROM event WHERE 1=1 ";

        if (filterMap.isEmpty()) {
            return jdbcTemplate.query(sql, new EventRowMapper());
        }

        Map<String, Object> params = new HashMap<>();
        for (String key : filterMap.keySet()) {
            sql += " AND " + key + " = :" + key + " ";
            params.put(key, filterMap.get(key));
        }

        return jdbcTemplate.query(sql, params, new EventRowMapper());
    }

}
