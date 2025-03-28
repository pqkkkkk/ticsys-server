package com.example.ticsys.sharedDto;

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
public class SharedPromotionDto {
    int id;
    int eventId;
    int minPriceToReach;
    int promoPercent;
    int voucherValue;
    String status;
    String type;
    LocalDate startDate;
    LocalDate endDate;
    int reduction;

}
