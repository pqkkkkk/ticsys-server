package com.example.ticsys.outbound.eventPublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.ticsys.outbound.event.OrderPaymentRequestEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderEventPublisherV1 implements IOrderEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic = "tsbank-order-payment";

    @Autowired
    public OrderEventPublisherV1(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public void SendOrderPaymentRequest(Integer orderId, Double orderPrice, Integer voucherOfUserId,
            String bankAccountId) {
        try{ 
        OrderPaymentRequestEvent event = OrderPaymentRequestEvent.builder()
                .orderId(orderId)
                .orderPrice(orderPrice)
                .voucherOfUserId(voucherOfUserId)
                .accountId(bankAccountId)
                .build();

        kafkaTemplate.send(topic, event);
        log.info("Sent OrderPaymentRequestEvent to Kafka topic: " + topic + ", event: " + event);
        }
        catch (Exception e) {
            log.error("Failed to send OrderPaymentRequestEvent to Kafka topic: " + topic + ", event: " + e.getMessage());
            throw e;
        }
    }

}
