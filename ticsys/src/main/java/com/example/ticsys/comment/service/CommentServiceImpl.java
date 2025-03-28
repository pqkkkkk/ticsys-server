package com.example.ticsys.comment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticsys.comment.dao.ICommentDao;
import com.example.ticsys.comment.model.Comment;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final ICommentDao commentDao;

    @Autowired
    public CommentServiceImpl(ICommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public int CreateComment(Comment comment) {
        try{
            return commentDao.CreateComment(comment);
        }
        catch(Exception e){
            log.error("Error in CreateComment of CommentService: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public List<Comment> GetComments(String senderId, int eventId, int parentId) {
        try{
            List<Comment> result =  commentDao.GetComments(senderId, eventId, parentId);

            for(Comment comment : result){
                comment.setChildCount(commentDao.CountChildOfComment(comment.getId()));
            }

            return result;
        }
        catch(Exception e){
            log.error("Error in GetComments of CommentService: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean UpdateComment(Comment comment) {
        try{
            return commentDao.UpdateComment(comment);
        }
        catch(Exception e){
            log.error("Error in UpdateComment of CommentService: " + e.getMessage());
            return false;
        }
    }

}
