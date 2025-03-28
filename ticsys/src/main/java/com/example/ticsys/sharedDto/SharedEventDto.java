package com.example.ticsys.sharedDto;

import java.sql.Time;
import java.time.LocalDate;

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
public class SharedEventDto {
    int id;
    String organizerId;
    String location;
    String description;
    String bannerPath;
    String name;
    String status;
    String category;
    LocalDate date;
    Time time;
}
