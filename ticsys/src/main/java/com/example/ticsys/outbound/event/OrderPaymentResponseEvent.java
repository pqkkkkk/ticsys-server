package com.example.ticsys.outbound.event;

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
public class OrderPaymentResponseEvent {
    Integer orderId;
    Double orderPrice;
    Integer voucherOfUserId;
    PaymentResult transactionResult;
    String message;
}
