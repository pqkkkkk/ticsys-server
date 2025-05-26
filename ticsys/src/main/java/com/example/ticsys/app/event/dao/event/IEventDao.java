package com.example.ticsys.app.event.dao.event;

import java.util.List;
import java.util.Map;

import com.example.ticsys.app.event.model.Event;

public interface IEventDao {
    public int CreateEvent(Event event);
    public Event GetEventById(int id);
    public List<Event> GetEvents(String category, String status, String organizerId);
    public List<Event> GetEvents(Map<String,Object> filterMap);
    public Map<String,Object> GetEventByRequiredFieldsList(List<String> requiredFields, int id);
}
