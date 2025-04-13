package com.example.ticsys.outbound.eventHandler;

import com.example.ticsys.outbound.event.OrderPaymentResponseEvent;

public interface IOrderEventHandler {
    public void HandleOrderPaymentResponseEvent(OrderPaymentResponseEvent event);
}
