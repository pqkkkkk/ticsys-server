package com.example.ticsys.promotion.model;

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
public class Promotion {
    Integer id;
    Integer eventId;
    Integer minPriceToReach;
    Integer promoPercent;
    Integer voucherValue;
    String status;
    String type;
    LocalDate startDate;
    LocalDate endDate;
}
