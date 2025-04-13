package com.example.ticsys.outbound.eventPublisher;

public interface IOrderEventPublisher {
    public void SendOrderPaymentRequest(Integer orderId, Double orderPrice,
                                            Integer voucherOfUserId, String bankAccountId);
}
