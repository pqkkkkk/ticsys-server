package com.example.ticsys.app.shared_dto;

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
public class SharedOrderDto {
    Integer id;
    Integer price;
    Integer ticketCount;
    String createdBy;
    Integer eventId;
    LocalDate dateCreatedAt;
    Time timeCreatedAt;
    String status;
    Integer promotionId;
}
