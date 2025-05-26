package com.example.ticsys.app.outbound.eventPublisher;

public interface IEventPublisher {
    public void SendOrderPaymentRequest(Integer orderId, Double orderPrice,
                                            Integer voucherOfUserId, String bankAccountId);
    public void SendLinkBankAccountRequest(String username, String bankAccountId,
                                            String bankAccountOwnerName);
}
