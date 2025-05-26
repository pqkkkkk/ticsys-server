package com.example.ticsys.app.comment.service;

import java.util.List;

import com.example.ticsys.app.comment.model.Comment;


public interface CommentService {
    int CreateComment(Comment comment);
    List<Comment> GetComments(String senderId, int eventId, int parentId);
    boolean UpdateComment(Comment comment);   
}
