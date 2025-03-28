package com.example.ticsys.event.model;

import java.sql.Time;
import java.time.LocalDate;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    int id;
    String organizerId;
    String location;
    String description;
    String bannerPath;
    String seatMapPath;
    String name;
    String status;
    String category;
    LocalDate date;
    Time time;
}
