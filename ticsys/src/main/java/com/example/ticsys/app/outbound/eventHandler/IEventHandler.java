package com.example.ticsys.app.outbound.eventHandler;

import com.example.ticsys.app.outbound.event.LinkBankAccountResult;
import com.example.ticsys.app.outbound.event.OrderPaymentResponse;

public interface IEventHandler {
    public void HandleOrderPaymentResponseEvent(OrderPaymentResponse event);
    public void HandleLinkBankAccountResponse(LinkBankAccountResult event); 
}
