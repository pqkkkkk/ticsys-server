package com.example.ticsys.app.event.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ticsys.app.event.dto.EventDto;
import com.example.ticsys.app.event.dto.request.EventRequest;
import com.example.ticsys.app.event.dto.response.EventResponse;
import com.example.ticsys.app.event.dto.response.GetEventsResponse;
import com.example.ticsys.app.event.model.Event;
import com.example.ticsys.app.event.model.Ticket;
import com.example.ticsys.app.event.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<EventResponse> CreateEvent(@RequestParam("event") String eventJson,
                                        @RequestParam("tickets") String ticketsJson,
                                        @RequestParam("banner") MultipartFile banner,
                                        @RequestParam(required = false,value = "seatMap") MultipartFile seatMap)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try{
            Event event = objectMapper.readValue(eventJson, Event.class);
            List<Ticket> tickets = objectMapper.readValue(ticketsJson, new TypeReference<List<Ticket>>() {});
            EventRequest eventRequest = EventRequest.builder().event(event).tickets(tickets).build();

            return ResponseEntity.ok(eventService.CreateEvent(eventRequest, banner, seatMap));
        }
        catch (JsonProcessingException e)
        {
            return ResponseEntity.ok(EventResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping
    public ResponseEntity<GetEventsResponse> GetEvents( @RequestParam (value = "include",required = false) String includeStr,
                                        @RequestParam (required = false) String organizerId,
                                        @RequestParam (required = false) String category,
                                        @RequestParam (required = false) String status)
    {
         Map<String,Object> filterMap = new HashMap<>();
        if(category != null){
            filterMap.put("category", category);
        }
        if(status != null){
            filterMap.put("status", status);
        }
        if(organizerId != null){
            filterMap.put("organizerId", organizerId);
        }
        GetEventsResponse result = eventService.GetEvents(includeStr, filterMap);

        if (result.getMessage().equals("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
            
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> GetEventById(@PathVariable int id,
                                 @RequestParam (value = "include",required = false) String includeStr)
    {
        EventDto result = eventService.GetEventById(id, includeStr);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
}
