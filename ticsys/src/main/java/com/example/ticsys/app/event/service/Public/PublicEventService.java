package com.example.ticsys.app.event.service.Public;


import java.util.Map;

import com.example.ticsys.app.shared_dto.SharedEventDto;
import com.example.ticsys.app.shared_dto.SharedTicketDto;

public interface PublicEventService {
    String GetUsernameOfEventOwner(int eventId);
    SharedTicketDto GetTicketById(int id);
    SharedEventDto GetEventById(int id);
    int UpdateTicketByRequiredFieldsList(Map<String, Object> newValues, int id);

}
