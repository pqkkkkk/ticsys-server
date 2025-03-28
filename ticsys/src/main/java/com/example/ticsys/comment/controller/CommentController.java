package com.example.ticsys.comment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticsys.comment.model.Comment;
import com.example.ticsys.comment.service.CommentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Comment>> GetComments(@RequestParam(required = false) String senderId,
                                                     @RequestParam(required = false) Integer eventId,
                                                     @RequestParam(required = false) Integer parentId) {
        if(parentId == null) {
            parentId = -1;
        }
        if (eventId == null) {
            eventId = -1;
        }
        List<Comment> comments = commentService.GetComments(senderId, eventId, parentId);
        if (comments == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(comments);
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'ORGANIZER')")
    public ResponseEntity<Integer> CreateComment(@RequestBody Comment comment) {
        int result = commentService.CreateComment(comment);
        if (result == -1) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }
    @PutMapping
    @PreAuthorize("@commentSecurityServiceImpl.CanCommandComment(#comment.senderId,#comment.eventId)")
    public ResponseEntity<String> UpdateComment(@RequestBody Comment comment) {
        boolean result = commentService.UpdateComment(comment);
        if (!result) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("success");
    }
}
