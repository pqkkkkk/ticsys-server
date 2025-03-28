package com.example.ticsys.event.service.Public;


import java.util.Map;

import com.example.ticsys.sharedDto.SharedEventDto;
import com.example.ticsys.sharedDto.SharedTicketDto;

public interface PublicEventService {
    String GetUsernameOfEventOwner(int eventId);
    SharedTicketDto GetTicketById(int id);
    SharedEventDto GetEventById(int id);
    int UpdateTicketByRequiredFieldsList(Map<String, Object> newValues, int id);

}
