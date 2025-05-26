package com.example.ticsys.app.event.dto.response;

import java.util.List;

import com.example.ticsys.app.event.dto.EventDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetEventsResponse {
    List<EventDto> eventDtos;
    String message;
}
