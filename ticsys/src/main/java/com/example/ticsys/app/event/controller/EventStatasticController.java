package com.example.ticsys.app.event.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticsys.app.event.dto.response.TimelyEventRevenueResponse;
import com.example.ticsys.app.event.dto.response.TimelyEventTicketCountResponse;
import com.example.ticsys.app.event.service.EventService;

@RestController
@RequestMapping("/api/event/statastics")
public class EventStatasticController {
    private final EventService eventService;

    @Autowired
    public EventStatasticController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimelyEventRevenueResponse> GetRevenueOfAllEvents(@RequestParam (required = false) String eventCategory,
                                                  @RequestParam(required = true, value = "startDate") String startDateString,
                                                  @RequestParam(required = true,value = "endDate") String endDateString,
                                                  @RequestParam(required = true) String statasticsUnit)
    {
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);

        TimelyEventRevenueResponse result = eventService.GetRevenue(eventCategory, startDate, endDate, statasticsUnit);

        if(result.getMessage().equals("success")){
            return ResponseEntity.ok(result);
        }
        else{
            return ResponseEntity.badRequest().body(result);
        }
    }
    @GetMapping("/ticketCount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimelyEventTicketCountResponse> GetTicketCountOfAllEvents(@RequestParam (required = false) String eventCategory,
                                                  @RequestParam(required = true, value = "startDate") String startDateString,
                                                  @RequestParam(required = true,value = "endDate") String endDateString,
                                                  @RequestParam(required = true) String statasticsUnit)
    {
        TimelyEventTicketCountResponse result = eventService.GetTicketCount(eventCategory, LocalDate.parse(startDateString), LocalDate.parse(endDateString), statasticsUnit);

        if(result.getMessage().equals("success")){
            return ResponseEntity.ok(result);
        }
        else{
            return ResponseEntity.badRequest().body(result);
        }
    }
    @GetMapping("/revenue/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurityService.CheckEventOwner(#eventId)")
    public ResponseEntity<?> GetRevenueOfSpecificEvent(@PathVariable Integer eventId,
                                                  @RequestParam(required = false) Integer ticketTypeId,
                                                  @RequestParam(required = true, value = "startDate") String startDateString,
                                                  @RequestParam(required = true,value = "endDate") String endDateString,
                                                  @RequestParam(required = true) String statasticsUnit)
    {
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);
        
        if(ticketTypeId == null){
            ticketTypeId = -1;
        }

        TimelyEventRevenueResponse result = eventService.GetRevenueOfSpecificEvent(eventId, ticketTypeId, startDate, endDate, statasticsUnit);

        if(result.getMessage().equals("success")){
            return ResponseEntity.ok(result);
        }
        else{
            return ResponseEntity.badRequest().body(result);
        }
    }
    @GetMapping("/ticketCount/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurityService.CheckEventOwner(#eventId)")
    public ResponseEntity<TimelyEventTicketCountResponse> GetTicketCountOfSpecificEvent(@PathVariable Integer eventId,
                                                  @RequestParam(required = false) Integer ticketTypeId,
                                                  @RequestParam(required = true, value = "startDate") String startDateString,
                                                  @RequestParam(required = true,value = "endDate") String endDateString,
                                                  @RequestParam(required = true) String statasticsUnit)
    {
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);

        if(ticketTypeId == null){
            ticketTypeId = -1;
        }

        TimelyEventTicketCountResponse result = eventService.GetTicketCountOfSpecificEvent(eventId, ticketTypeId, startDate, endDate, statasticsUnit);

        if(result.getMessage().equals("success")){
            return ResponseEntity.ok(result);
        }
        else{
            return ResponseEntity.badRequest().body(result);
        }
    }

}
