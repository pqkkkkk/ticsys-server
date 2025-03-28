package com.example.ticsys.event.service.Public;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.ticsys.event.dao.event.IEventDao;
import com.example.ticsys.event.dao.event.query.IEventQueryDao;
import com.example.ticsys.event.dao.ticket.ITicketDao;
import com.example.ticsys.event.model.Event;
import com.example.ticsys.event.model.Ticket;
import com.example.ticsys.sharedDto.SharedEventDto;
import com.example.ticsys.sharedDto.SharedTicketDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Primary
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {
    private final ITicketDao ticketDao;
    private final IEventDao eventDao;
    private final IEventQueryDao eventQueryDao;

    @Autowired
    public PublicEventServiceImpl(ITicketDao ticketDao, IEventDao eventDao,
                                IEventQueryDao eventQueryDao) {
        this.ticketDao = ticketDao;
        this.eventDao = eventDao;
        this.eventQueryDao = eventQueryDao;
    }
    @Override
    public SharedTicketDto GetTicketById(int id) {
        try{
            Ticket ticket = ticketDao.GetTicketById(id);

            if (ticket == null) {
                return null;
            }

            SharedTicketDto result = SharedTicketDto.builder()
                    .id(ticket.getId())
                    .eventId(ticket.getEventId())
                    .price(ticket.getPrice())
                    .service(ticket.getService())
                    .name(ticket.getName())
                    .quantity(ticket.getQuantity())
                    .build();
            return result;
        }
        catch (Exception e){
            return null;
        }
    }
    @Override
    public int UpdateTicketByRequiredFieldsList(Map<String, Object> newValues, int id) {
        try{
            int result =  ticketDao.UpdateTicketByRequiredFieldsList(newValues, id);
            return result;
        }
        catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }
    @Override
    public SharedEventDto GetEventById(int id) {
        try{
            Event event = eventDao.GetEventById(id);

            if (event == null) {
                return null;
            }

            SharedEventDto result = SharedEventDto.builder()
                    .id(event.getId())
                    .name(event.getName())
                    .bannerPath(event.getBannerPath())
                    .date(event.getDate())
                    .time(event.getTime())
                    .location(event.getLocation())
                    .organizerId(event.getOrganizerId())
                    .build();

            return result;
        }
        catch (Exception e){
            return null;
        }
    }
    @Override
    public String GetUsernameOfEventOwner(int eventId) {
        try{
            String result = eventQueryDao.GetUsernameOfEventOwner(eventId);
            return result;
        }
        catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

}
