package com.example.ticsys.comment.service.security;


public interface CommentSecurityService {
    public boolean CanCommandComment(String userId,Integer eventId);
}
