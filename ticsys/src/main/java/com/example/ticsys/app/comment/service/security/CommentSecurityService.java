package com.example.ticsys.app.comment.service.security;


public interface CommentSecurityService {
    public boolean CanCommandComment(String userId,Integer eventId);
}
