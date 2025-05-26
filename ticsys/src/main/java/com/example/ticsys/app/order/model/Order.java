package com.example.ticsys.app.order.model;

import java.sql.Time;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Order {
    Integer id;
    Integer price;
    String createdBy;
    Integer eventId;
    LocalDate dateCreatedAt;
    Time timeCreatedAt;
    String status;
    Integer promotionId;
    Integer voucherOfUserId;
}
