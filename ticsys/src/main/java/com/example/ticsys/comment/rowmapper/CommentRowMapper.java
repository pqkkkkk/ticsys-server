package com.example.ticsys.comment.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.example.ticsys.comment.model.Comment;

public class CommentRowMapper implements RowMapper<Comment> {

    @Override
    public Comment mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return Comment.builder()
                .id(rs.getInt("id"))
                .content(rs.getString("content"))
                .senderId(rs.getString("senderId"))
                .eventId(rs.getInt("eventId"))
                .parentId(rs.getInt("parentId"))
                .dateCreatedAt(rs.getDate("dateCreatedAt") == null ? null : rs.getDate("dateCreatedAt").toLocalDate())
                .timeCreatedAt(rs.getTime("timeCreatedAt") == null ? null : rs.getTime("timeCreatedAt"))
                .build();
    }

}
