package com.example.ticsys.event.dto;

import java.util.List;

import com.example.ticsys.event.model.Event;
import com.example.ticsys.event.model.Ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    Event event;
    List<Ticket> tickets;
    int minPriceOfTicket;
}
