package com.example.ticsys.sharedDto;

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
public class SharedVoucherOfUserDto {
    int id;
    int voucherValue;
    String userId;
    int promotionId;
    String status;
}
