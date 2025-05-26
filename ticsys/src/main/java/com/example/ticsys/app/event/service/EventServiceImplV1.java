package com.example.ticsys.app.event.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ticsys.app.event.dao.event.IEventDao;
import com.example.ticsys.app.event.dto.EventDto;
import com.example.ticsys.app.event.dto.request.EventRequest;
import com.example.ticsys.app.event.dto.response.EventResponse;
import com.example.ticsys.app.event.dto.response.GetEventsResponse;
import com.example.ticsys.app.event.dto.response.TimelyEventRevenueResponse;
import com.example.ticsys.app.event.dto.response.TimelyEventTicketCountResponse;
import com.example.ticsys.app.event.model.Event;
import com.example.ticsys.external_service.storage.CloudinaryService;

@Service
public class EventServiceImplV1 implements EventService {
    private final IEventDao eventDao;
    private final CloudinaryService cloudinaryService;
    public EventServiceImplV1(IEventDao eventDao, CloudinaryService cloudinaryService) {
        this.eventDao = eventDao;
        this.cloudinaryService = cloudinaryService;
    }
    public EventResponse CreateEvent (EventRequest eventRequest, MultipartFile banner, MultipartFile seatMap)
    {
        String bannerPath = cloudinaryService.uploadFile(banner);
        String seatMapPath = cloudinaryService.uploadFile(seatMap);
        if(!bannerPath.equals("") && !seatMapPath.equals(""))
        {
            eventRequest.getEvent().setBannerPath(bannerPath);
            eventRequest.getEvent().setSeatMapPath(seatMapPath);

            int eventId = eventDao.CreateEvent(eventRequest.getEvent());
            if(eventId > 0)
            {
                return EventResponse.builder().message("success").build();
            }
            else
            {
                return EventResponse.builder().message("fail").build();
            }
        }
        else
        {
            return EventResponse.builder().message("file error").build();
        }
    }
    public EventDto GetEventById(int id, String includeStr)
    {
        try{
            Event event = eventDao.GetEventById(id);
            EventDto eventDto = EventDto.builder().event(event).build();
            return eventDto;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public GetEventsResponse GetEvents(String includeStr, Map<String, Object> filterMap)
    {
        return null;
    }
    @Override
    public TimelyEventRevenueResponse GetRevenue(String eventCategory,
            LocalDate startDate, LocalDate endDate, String statasticsUnit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetRevenue'");
    }
    @Override
    public TimelyEventTicketCountResponse GetTicketCount(String eventCategory, LocalDate startDate, LocalDate endDate,
            String statasticsUnit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetTicketCount'");
    }
    @Override
    public TimelyEventRevenueResponse GetRevenueOfSpecificEvent(Integer eventId, Integer ticketTypeId,
            LocalDate startDate, LocalDate endDate, String statasticsUnit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetRevenueOfSpecificEvent'");
    }
    @Override
    public TimelyEventTicketCountResponse GetTicketCountOfSpecificEvent(Integer eventId, Integer ticketTypeId,
            LocalDate startDate, LocalDate endDate, String statasticsUnit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetTicketCountOfSpecificEvent'");
    }

}
