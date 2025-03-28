package com.example.ticsys.comment.service.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.ticsys.event.service.Public.PublicEventService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentSecurityServiceImpl implements CommentSecurityService {
    private final PublicEventService publicEventService;

    @Autowired
    public CommentSecurityServiceImpl(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }
    private boolean IsEventOwner(String userId, Integer eventId) {
        try{
            if(userId == null || eventId == null){
                return false;
            }
            String eventOwner = publicEventService.GetUsernameOfEventOwner(eventId);
            return eventOwner.equals(userId);
        }
        catch(Exception e){
            log.error("Error in CommentSecurityServiceImpl.IsEventOwner: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean CanCommandComment(String userId, Integer eventId) {
        try{
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            List<String> currentRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                                        .stream().map(r -> r.getAuthority()).collect(Collectors.toList());
            
            if(currentRoles.contains("ROLE_ADMIN")){
                return true;
            }
            else if (currentRoles.contains("ROLE_ORGANIZER")){
                return IsEventOwner(currentUsername, eventId);
            }
            else if (currentRoles.contains("ROLE_USER")){
                if(currentUsername.equals(userId)){
                    return true;
                }
            }

            return false;
        }
        catch(Exception e){
            log.error("Error in CommentSecurityServiceImpl.CanCommandComment: " + e.getMessage());
            return false;
        }
    }
}
