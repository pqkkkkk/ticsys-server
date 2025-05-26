package com.example.ticsys.app.event.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.ticsys.app.event.dao.event.query.IEventQueryDao;

@Service
public class EventSecurityServiceImpl implements EventSecurityService {
    private final IEventQueryDao eventQueryDao;

    @Autowired
    public EventSecurityServiceImpl(IEventQueryDao eventQueryDao) {
        this.eventQueryDao = eventQueryDao;
    }
    @Override
    public boolean CheckEventOwner(Integer eventId) {
        try{
            String usernameOfEventOwner = eventQueryDao.GetUsernameOfEventOwner(eventId);

            if(usernameOfEventOwner == null){
                return false;
            }
            String usernameOfCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName();

            return usernameOfCurrentUser.equals(usernameOfEventOwner);
        }
        catch (Exception e){
            return false;
        }
    }

}
