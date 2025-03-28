package com.example.ticsys.event.dto.response;

import java.util.List;

import com.example.ticsys.event.dto.TimelyEventDataDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TimelyEventRevenueResponse {
    List<TimelyEventDataDto> revenues;
    Integer totalRevenue;
    String message;
}
