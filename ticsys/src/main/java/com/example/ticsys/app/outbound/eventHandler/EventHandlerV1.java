package com.example.ticsys.app.outbound.eventHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.ticsys.app.order.service.OrderService;
import com.example.ticsys.app.outbound.event.LinkBankAccountResult;
import com.example.ticsys.app.outbound.event.OrderPaymentResponse;
import com.example.ticsys.app.outbound.event.PaymentResult;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class EventHandlerV1 implements IEventHandler {
    private final OrderService orderService;

    @Autowired
    public EventHandlerV1(OrderService orderService) {
        this.orderService = orderService;
    }
    @Override
    @KafkaListener(topics = "ticsys-order-payment-result", groupId = "ticsys")
    public void HandleOrderPaymentResponseEvent(OrderPaymentResponse event) {
        try{
            PaymentResult transactionResult = event.getTransactionResult();

            if(transactionResult == PaymentResult.SUCCESS) {
                orderService.ConfirmOrder(event.getOrderId(), event.getVoucherOfUserId(), event.getOrderPrice().intValue());
                log.info("Order payment successful for order ID: " + event.getOrderId() + ", voucher ID: " + event.getVoucherOfUserId() + ", price: " + event.getOrderPrice());
            }
            else{
                orderService.CancelOrder(event.getOrderId());
                log.info("Order payment failed for order ID: " + event.getOrderId() + ", voucher ID: " + event.getVoucherOfUserId() + ", price: " + event.getOrderPrice() + ", reason: " + transactionResult);
            }
        }
        catch (Exception e) {
            log.error("Failed to handle OrderPaymentResponseEvent: " + e.getMessage());
            throw e;
        }
    }
    @Override
    @KafkaListener(topics = "ticsys-link-bank-account-result", groupId = "ticsys")
    public void HandleLinkBankAccountResponse(LinkBankAccountResult event) {
        try{
            log.info("Received LinkBankAccountResult: " + event);
        }
        catch (Exception e) {
            log.error("Failed to handle LinkBankAccountResponse: " + e.getMessage());
            throw e;
        }
    }

}
