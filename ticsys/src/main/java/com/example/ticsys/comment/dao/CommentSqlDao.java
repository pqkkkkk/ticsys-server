package com.example.ticsys.comment.dao;

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

import com.example.ticsys.comment.model.Comment;
import com.example.ticsys.comment.rowmapper.CommentRowMapper;

@Repository
public class CommentSqlDao implements ICommentDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public CommentSqlDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public int CreateComment(Comment comment) {
        String sql = """
                INSERT INTO [comment] (content, senderId, eventId, parentId,dateCreatedAt,timeCreatedAt)
                 values (:content, :senderId, :eventId, :parentId, GETDATE(), GETDATE())
                """;
        
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("content", comment.getContent());
        paramMap.put("senderId", comment.getSenderId());
        paramMap.put("eventId", comment.getEventId());
        paramMap.put("parentId", comment.getParentId() == 0? null: comment.getParentId());

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
    public List<Comment> GetComments(String senderId, int eventId, int parentId) {
        String sql = "SELECT * FROM [comment] WHERE 1=1 ";
        Map<String, Object> paramMap = new HashMap<>();

        if (senderId != null) {
            sql += "AND senderId = :senderId ";
            paramMap.put("senderId", senderId);
        }
        if (eventId != -1) {
            sql += "AND eventId = :eventId ";
            paramMap.put("eventId", eventId);
        }
        if (parentId != -1) {
            sql += "AND parentId = :parentId ";
            paramMap.put("parentId", parentId);
        }
        else{
            sql += "AND parentId IS NULL ";
        }

        return jdbcTemplate.query(sql, paramMap, new CommentRowMapper());
    }
    @Override
    public boolean UpdateComment(Comment comment) {
        String sql = """
                UPDATE [comment] SET content = :content WHERE id = :id
                """;
        
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("content", comment.getContent());
        paramMap.put("id", comment.getId());

        return jdbcTemplate.update(sql, paramMap) > 0;
    }
    @Override
    public int CountChildOfComment(int id) {
        String sql = "SELECT COUNT(*) FROM [comment] WHERE parentId = :id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        Integer result =  jdbcTemplate.queryForObject(sql, paramMap, Integer.class);

        return result == null? 0: result;
    }

}
