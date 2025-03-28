package com.example.ticsys.order.dto;

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
public class OrderInfoForCheckAuthorityDto {
    Integer orderId;
    String createdBy;
    Integer eventId;
    String eventOwner;
}
