package com.example.ticsys.outbound.eventHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.ticsys.order.service.OrderService;
import com.example.ticsys.outbound.event.OrderPaymentResponseEvent;
import com.example.ticsys.outbound.event.PaymentResult;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class OrderEventHandlerV1 implements IOrderEventHandler {
    private final OrderService orderService;

    @Autowired
    public OrderEventHandlerV1(OrderService orderService) {
        this.orderService = orderService;
    }
    @Override
    @KafkaListener(topics = "ticsys-order-payment-result", groupId = "ticsys")
    public void HandleOrderPaymentResponseEvent(OrderPaymentResponseEvent event) {
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

}
