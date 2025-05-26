package com.example.ticsys.app.event.service;


import java.time.LocalDate;
import java.util.Map;


import org.springframework.web.multipart.MultipartFile;

import com.example.ticsys.app.event.dto.EventDto;
import com.example.ticsys.app.event.dto.request.EventRequest;
import com.example.ticsys.app.event.dto.response.EventResponse;
import com.example.ticsys.app.event.dto.response.GetEventsResponse;
import com.example.ticsys.app.event.dto.response.TimelyEventRevenueResponse;
import com.example.ticsys.app.event.dto.response.TimelyEventTicketCountResponse;

public interface EventService {
    public EventResponse CreateEvent(EventRequest eventRequest, MultipartFile banner, MultipartFile seatMap);
    public EventDto GetEventById(int id, String includeStr);
    public GetEventsResponse GetEvents(String includeStr, Map<String, Object> filterMap);

    public TimelyEventRevenueResponse GetRevenue(String eventCategory,
                                                 LocalDate startDate, LocalDate endDate, String statasticsUnit);
    public TimelyEventTicketCountResponse GetTicketCount(String eventCategory,
                                                     LocalDate startDate, LocalDate endDate, String statasticsUnit);
    public TimelyEventRevenueResponse GetRevenueOfSpecificEvent(Integer eventId, Integer ticketTypeId,
                                                            LocalDate startDate, LocalDate endDate, String statasticsUnit);
    public TimelyEventTicketCountResponse GetTicketCountOfSpecificEvent(Integer eventId, Integer ticketTypeId,
                                                            LocalDate startDate, LocalDate endDate, String statasticsUnit);
}
