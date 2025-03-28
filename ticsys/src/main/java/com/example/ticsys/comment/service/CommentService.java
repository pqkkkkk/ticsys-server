package com.example.ticsys.comment.service;

import java.util.List;
import com.example.ticsys.comment.model.Comment;


public interface CommentService {
    int CreateComment(Comment comment);
    List<Comment> GetComments(String senderId, int eventId, int parentId);
    boolean UpdateComment(Comment comment);   
}
