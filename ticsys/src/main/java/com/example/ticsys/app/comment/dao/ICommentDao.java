package com.example.ticsys.app.comment.dao;

import java.util.List;

import com.example.ticsys.app.comment.model.Comment;

public interface ICommentDao {
    int CreateComment(Comment comment);
    List<Comment> GetComments(String senderId, int eventId, int parentId);
    boolean UpdateComment(Comment comment);
    int CountChildOfComment(int id);
}
