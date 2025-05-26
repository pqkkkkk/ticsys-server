package com.example.ticsys.app.event.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.example.ticsys.app.event.dao.event.IEventDao;
import com.example.ticsys.app.event.dao.ticket.ITicketDao;
import com.example.ticsys.app.event.dto.EventDto;
import com.example.ticsys.app.event.dto.TimelyEventDataDto;
import com.example.ticsys.app.event.dto.request.EventRequest;
import com.example.ticsys.app.event.dto.response.EventResponse;
import com.example.ticsys.app.event.dto.response.GetEventsResponse;
import com.example.ticsys.app.event.dto.response.TimelyEventRevenueResponse;
import com.example.ticsys.app.event.dto.response.TimelyEventTicketCountResponse;
import com.example.ticsys.app.event.model.Event;
import com.example.ticsys.app.event.model.Ticket;
import com.example.ticsys.app.order.service.Public.PublicOrderService;
import com.example.ticsys.app.shared_dto.SharedOrderDto;
import com.example.ticsys.external_service.redis.RedisService;
import com.example.ticsys.external_service.storage.CloudinaryService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Primary
@Service
@Slf4j
public class EventServiceImplV2 implements EventService {
    private final IEventDao eventDao;
    private final ITicketDao ticketDao;
    private final CloudinaryService cloudinaryService;
    private final RedisService redisService;
    private final PublicOrderService publicOrderService;
    @Autowired
    public EventServiceImplV2(IEventDao eventDao, ITicketDao ticketDao,
                                 CloudinaryService cloudinaryService,
                                 RedisService redisService, PublicOrderService publicOrderService) {
        this.eventDao = eventDao;
        this.ticketDao = ticketDao;
        this.cloudinaryService = cloudinaryService;
        this.redisService = redisService;
        this.publicOrderService = publicOrderService;
    }
    @Override
    @Transactional
    public EventResponse CreateEvent(EventRequest eventRequest, MultipartFile banner, MultipartFile seatMap) {
        String bannerPath = "";
        String seatMapPath = "";
        try{
            if(banner != null){
                bannerPath = cloudinaryService.uploadFile(banner);
            }
            else{
                bannerPath = "empty";
            }
            if(seatMap != null){
                seatMapPath = cloudinaryService.uploadFile(seatMap);
            }
            else{
                seatMapPath = "empty";
            }

            if(bannerPath.equals("") || seatMapPath.equals("")){
                throw new Exception("Failed to upload file");
            }
            eventRequest.getEvent().setBannerPath(bannerPath.equals("empty") ? null : bannerPath);
            eventRequest.getEvent().setSeatMapPath(seatMapPath.equals("empty") ? null : seatMapPath);
            int eventId = eventDao.CreateEvent(eventRequest.getEvent());
            if(eventId == -1){
                throw new Exception("Failed to create event");
            }

            for(Ticket ticket : eventRequest.getTickets()){
                ticket.setEventId(eventId);
                if(!ticketDao.AddTicket(ticket)){
                    throw new Exception("Failed to create ticket");
                }
            }

            return EventResponse.builder().message("success").build();
        }
        catch (Exception e){
            String deleteBannerResult = cloudinaryService.deleteFile(bannerPath);
            String deleteSeatMapResult = cloudinaryService.deleteFile(seatMapPath);

            log.error("Failed to create event: " + e.getMessage());
            log.error("Delete banner result: " + deleteBannerResult);
            log.error("Delete seat map result: " + deleteSeatMapResult);

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return EventResponse.builder().message(e.getMessage()).build();
        }
    }
    @Override
    public EventDto GetEventById(int id, String includeStr) {
        try{
            Event event = eventDao.GetEventById(id);
            EventDto eventDto = EventDto.builder().event(event).build();

            List<Ticket> tickets = ticketDao.GetTicketsOfEvent(event.getId());

            int minPriceOfTicket = tickets.stream().mapToInt(Ticket::getPrice)
                                    .min()
                                    .orElse(0);

            eventDto.setMinPriceOfTicket(minPriceOfTicket);

            if(includeStr != null && includeStr.contains("tickets")){
                eventDto.setTickets(tickets);
            }

            return eventDto;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    @Override
    public GetEventsResponse GetEvents(String includeStr, Map<String, Object> filterMap) {
        try{
            // if(redisService.getData("events") != null){
            //     log.info("Get events from Redis");
            //     List<EventDto> eventDtos = (List<EventDto>) redisService.getData("events");
            //     return GetEventsResponse.builder().message("success").eventDtos(eventDtos).build();
            // }

            log.info("Get events from database");
            List<EventDto> eventDtos = new ArrayList<>();
            List<Event> events = eventDao.GetEvents(filterMap);

            for(Event event : events)
            {
                EventDto eventDto = new EventDto();
                eventDto.setEvent(event);

                List<Ticket> tickets = ticketDao.GetTicketsOfEvent(event.getId());

                int minPriceOfTicket = tickets.stream().mapToInt(Ticket::getPrice)
                                    .min()
                                    .orElse(0);
                eventDto.setMinPriceOfTicket(minPriceOfTicket);

                if(includeStr != null && includeStr.contains("tickets")){
                    eventDto.setTickets(tickets);
                }

                eventDtos.add(eventDto);
            }

            return GetEventsResponse.builder().message("success").eventDtos(eventDtos).build();
        }
        catch (Exception e)
        {
            return GetEventsResponse.builder().message(e.getMessage()).eventDtos(null).build();
        }
    }
    // @PostConstruct
    // public void AddEventsToRedis(){
    //     List<EventDto> eventDtos = new ArrayList<>();
    //     List<Event> events = eventDao.GetEvents(null, null);

    //     for(Event event : events)
    //     {
    //         EventDto eventDto = new EventDto();
    //         eventDto.setEvent(event);

    //         List<Ticket> tickets = ticketDao.GetTicketsOfEvent(event.getId());

    //         int minPriceOfTicket = tickets.stream().mapToInt(Ticket::getPrice)
    //                             .min()
    //                             .orElse(0);
    //         eventDto.setMinPriceOfTicket(minPriceOfTicket);

    //         eventDto.setTickets(tickets);

    //         eventDtos.add(eventDto);
    //     }

    //     redisService.saveData("events", eventDtos);
    // }
    private String GetLabel(String statasticsUnit, LocalDate date){
       String label = "";

       switch (statasticsUnit) {
        case "date":
            label = date.toString();
            break;
        case "month":
            label = date.getYear() + "-" + String.format("%02d", date.getMonthValue());
            break;
        case "year":
            label = date.getYear() + "";
            break;
        default:
            break;
       }

        return label;
    }
    private Map<String, Object> GenerateRevenueMap(LocalDate startDate, LocalDate endDate, String statasticsUnit){
        Map<String, Object> revenueMap = new HashMap<>();
        LocalDate currentDate = startDate;

        switch (statasticsUnit) {
            case "date":
                while(currentDate.isBefore(endDate)){
                    revenueMap.put(currentDate.toString(), 0);
                    currentDate = currentDate.plusDays(1);
                }
                break;
            case "month":
                while(currentDate.isBefore(endDate)){
                    revenueMap.put(currentDate.getYear() + "-" + String.format("%02d", currentDate.getMonthValue()), 0);
                    currentDate = currentDate.withDayOfMonth(1).plusMonths(1);
                }
                break;
            case "year":
                while(currentDate.isBefore(endDate)){
                    revenueMap.put(currentDate.getYear() + "", 0);
                    currentDate = currentDate.withDayOfYear(1).plusYears(1);
                }
                break;
            default:
                break;
        }
        return revenueMap;
    }
    @Override
    public TimelyEventRevenueResponse GetRevenue(String eventCategory,
                                        LocalDate startDate, LocalDate endDate, String statasticsUnit) {
        try{
            if(startDate.isAfter(endDate)){
               throw new Exception("Invalid date range");
            }
            if(!statasticsUnit.equals("date") && !statasticsUnit.equals("month") && !statasticsUnit.equals("year")){
               throw new Exception("Invalid statastics unit");
            }

            TimelyEventRevenueResponse result = TimelyEventRevenueResponse.builder()
                                                .totalRevenue(null)
                                                .revenues(null)
                                                .message("error").build();
                                        
            List<SharedOrderDto> orders = publicOrderService.GetOrders(null, null, null, "PAID", -1);
            orders = orders.stream().filter(order -> order.getDateCreatedAt().isAfter(startDate) && order.getDateCreatedAt().isBefore(endDate)).toList();

            Integer totalRevenue = 0;
            Map<String,Object> revenueMap = GenerateRevenueMap(startDate, endDate, statasticsUnit);

            for(SharedOrderDto order : orders){
                String label = GetLabel(statasticsUnit, order.getDateCreatedAt());

                if(revenueMap.containsKey(label)){
                    revenueMap.put(label, (Integer)revenueMap.get(label) + order.getPrice());
                }
                else{
                    revenueMap.put(label, order.getPrice());
                }

                totalRevenue += order.getPrice();   
            }

            List<TimelyEventDataDto> revenues = new ArrayList<>();
            for(Map.Entry<String, Object> entry : revenueMap.entrySet()){
                revenues.add(TimelyEventDataDto.builder()
                                .label(entry.getKey())
                                .value((Integer)entry.getValue())
                                .build());
            }

            result.setRevenues(revenues);
            result.setMessage("success");
            result.setTotalRevenue(totalRevenue);

            return result;
        }
        catch(Exception e){
            log.error("Error: " + e.getMessage());

            return TimelyEventRevenueResponse.builder()
                        .totalRevenue(null)
                        .revenues(null)        
                        .message("error").build();
        }
    }
    @Override
    public TimelyEventTicketCountResponse GetTicketCount(String eventCategory, LocalDate startDate, LocalDate endDate,
            String statasticsUnit) {
                try{
                    if(startDate.isAfter(endDate)){
                       throw new Exception("Invalid date range");
                    }
                    if(!statasticsUnit.equals("date") && !statasticsUnit.equals("month") && !statasticsUnit.equals("year")){
                       throw new Exception("Invalid statastics unit");
                    }
        
                    TimelyEventTicketCountResponse result = TimelyEventTicketCountResponse.builder()
                                                        .totalTicketCount(0)
                                                        .ticketCounts(null)
                                                        .message("error").build();
                                                
                    List<SharedOrderDto> orders = publicOrderService.GetOrders(null, null, null, "PAID", -1,-1);
                    orders = orders.stream().filter(order -> order.getDateCreatedAt().isAfter(startDate) && order.getDateCreatedAt().isBefore(endDate)).toList();
        
                    Integer totalTicketCount = 0;
                    Map<String,Object> ticketCountMap = GenerateRevenueMap(startDate, endDate, statasticsUnit);
        
                    for(SharedOrderDto order : orders){
                        String label = GetLabel(statasticsUnit, order.getDateCreatedAt());
        
                        if(ticketCountMap.containsKey(label)){
                            ticketCountMap.put(label, (Integer)ticketCountMap.get(label) + order.getTicketCount());
                        }
                        else{
                            ticketCountMap.put(label, order.getTicketCount());
                        }
        
                        totalTicketCount += order.getTicketCount();   
                    }
        
                    List<TimelyEventDataDto> ticketCounts = new ArrayList<>();
                    for(Map.Entry<String, Object> entry : ticketCountMap.entrySet()){
                        ticketCounts.add(TimelyEventDataDto.builder()
                                        .label(entry.getKey())
                                        .value((Integer)entry.getValue())
                                        .build());
                    }
        
                    result.setTicketCounts(ticketCounts);
                    result.setMessage("success");
                    result.setTotalTicketCount(totalTicketCount);
        
                    return result;
                }
                catch(Exception e){
                    log.error("Error: " + e.getMessage());
        
                    return TimelyEventTicketCountResponse.builder()
                                .totalTicketCount(null)
                                .ticketCounts(null)        
                                .message("error").build();
                }
    }
    @Override
    public TimelyEventRevenueResponse GetRevenueOfSpecificEvent(Integer eventId, Integer ticketTypeId,
            LocalDate startDate, LocalDate endDate, String statasticsUnit) {
        try{
            if(startDate.isAfter(endDate)){
                throw new Exception("Invalid date range");
            }
            if(!statasticsUnit.equals("date") && !statasticsUnit.equals("month") && !statasticsUnit.equals("year")){
                throw new Exception("Invalid statastics unit");
            }

            TimelyEventRevenueResponse result = TimelyEventRevenueResponse.builder()
                                                .totalRevenue(null)
                                                .revenues(null)
                                                .message("error").build();
                                        
            List<SharedOrderDto> orders = publicOrderService.GetOrders(null, null, null, "PAID", eventId);
            orders = orders.stream().filter(order -> order.getDateCreatedAt().isAfter(startDate) && order.getDateCreatedAt().isBefore(endDate)).toList();

            Map<String,Object> revenueMap = GenerateRevenueMap(startDate, endDate, statasticsUnit);
            Integer totalRevenue = 0;

            for(SharedOrderDto order : orders){
                String label = GetLabel(statasticsUnit, order.getDateCreatedAt());

                if(revenueMap.containsKey(label)){
                    revenueMap.put(label, (Integer)revenueMap.get(label) + order.getPrice());
                }
                else{
                    revenueMap.put(label, order.getPrice());
                }

                totalRevenue += order.getPrice();
            }

            List<TimelyEventDataDto> revenues = new ArrayList<>();
            for(Map.Entry<String, Object> entry : revenueMap.entrySet()){
                revenues.add(TimelyEventDataDto.builder()
                                .label(entry.getKey())
                                .value((Integer)entry.getValue())
                                .build());
            }

            result.setRevenues(revenues);
            result.setMessage("success");
            result.setTotalRevenue(totalRevenue);

            return result;
        }
        catch(Exception e){
            log.error("Error: " + e.getMessage());

            return TimelyEventRevenueResponse.builder()
                        .totalRevenue(null)
                        .revenues(null)        
                        .message("error").build();
        }
    }
    @Override
    public TimelyEventTicketCountResponse GetTicketCountOfSpecificEvent(Integer eventId, Integer ticketTypeId,
                                                            LocalDate startDate, LocalDate endDate, String statasticsUnit) {
                try{
                    if(startDate.isAfter(endDate)){
                       throw new Exception("Invalid date range");
                    }
                    if(!statasticsUnit.equals("date") && !statasticsUnit.equals("month") && !statasticsUnit.equals("year")){
                       throw new Exception("Invalid statastics unit");
                    }
        
                    TimelyEventTicketCountResponse result = TimelyEventTicketCountResponse.builder()
                                                        .totalTicketCount(0)
                                                        .ticketCounts(null)
                                                        .message("error").build();
                                                
                    List<SharedOrderDto> orders = publicOrderService.GetOrders(null, null, null, "PAID", eventId,ticketTypeId);
                    orders = orders.stream().filter(order -> order.getDateCreatedAt().isAfter(startDate) && order.getDateCreatedAt().isBefore(endDate)).toList();
        
                    Integer totalTicketCount = 0;
                    Map<String,Object> ticketCountMap = GenerateRevenueMap(startDate, endDate, statasticsUnit);
        
                    for(SharedOrderDto order : orders){
                        String label = GetLabel(statasticsUnit, order.getDateCreatedAt());
        
                        if(ticketCountMap.containsKey(label)){
                            ticketCountMap.put(label, (Integer)ticketCountMap.get(label) + order.getTicketCount());
                        }
                        else{
                            ticketCountMap.put(label, order.getTicketCount());
                        }
        
                        totalTicketCount += order.getTicketCount();   
                    }
        
                    List<TimelyEventDataDto> ticketCounts = new ArrayList<>();
                    for(Map.Entry<String, Object> entry : ticketCountMap.entrySet()){
                        ticketCounts.add(TimelyEventDataDto.builder()
                                        .label(entry.getKey())
                                        .value((Integer)entry.getValue())
                                        .build());
                    }
        
                    result.setTicketCounts(ticketCounts);
                    result.setMessage("success");
                    result.setTotalTicketCount(totalTicketCount);
        
                    return result;
                }
                catch(Exception e){
                    log.error("Error: " + e.getMessage());
        
                    return TimelyEventTicketCountResponse.builder()
                                .totalTicketCount(null)
                                .ticketCounts(null)        
                                .message("error").build();
                }
    }
}
