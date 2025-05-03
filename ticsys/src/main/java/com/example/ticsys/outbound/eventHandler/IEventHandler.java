package com.example.ticsys.outbound.eventHandler;

import com.example.ticsys.outbound.event.LinkBankAccountResult;
import com.example.ticsys.outbound.event.OrderPaymentResponse;

public interface IEventHandler {
    public void HandleOrderPaymentResponseEvent(OrderPaymentResponse event);
    public void HandleLinkBankAccountResponse(LinkBankAccountResult event); 
}
