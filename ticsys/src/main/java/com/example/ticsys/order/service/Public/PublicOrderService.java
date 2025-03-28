package com.example.ticsys.order.service.Public;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import com.example.ticsys.sharedDto.SharedOrderDto;

public interface PublicOrderService {
    List<SharedOrderDto> GetOrders(String userId, LocalDate dateCreated, Time timeCreated, String status, Integer eventId);
    List<SharedOrderDto> GetOrders(String userId, LocalDate dateCreated, Time timeCreated, String status, Integer eventId,
                                   Integer ticketTypeId);
                
}
