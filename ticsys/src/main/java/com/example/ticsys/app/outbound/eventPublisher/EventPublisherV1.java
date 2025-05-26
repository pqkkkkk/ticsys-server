package com.example.ticsys.app.outbound.eventPublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.ticsys.app.outbound.event.LinkBankAccountEvent;
import com.example.ticsys.app.outbound.event.OrderPaymentEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventPublisherV1 implements IEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String orderPaymentTopic = "tsbank-order-payment";
    private final String linkBankAccountTopic = "tsbank-link-bank-account";

    @Autowired
    public EventPublisherV1(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public void SendOrderPaymentRequest(Integer orderId, Double orderPrice, Integer voucherOfUserId,
            String bankAccountId) {
        try{ 
        OrderPaymentEvent event = OrderPaymentEvent.builder()
                .orderId(orderId)
                .orderPrice(orderPrice)
                .voucherOfUserId(voucherOfUserId)
                .accountId(bankAccountId)
                .build();

        kafkaTemplate.send(orderPaymentTopic, event);
        log.info("Sent OrderPaymentRequestEvent to Kafka topic: " + orderPaymentTopic + ", event: " + event);
        }
        catch (Exception e) {
            log.error("Failed to send OrderPaymentRequestEvent to Kafka topic: " + orderPaymentTopic + ", event: " + e.getMessage());
            throw e;
        }
    }
    @Override
    public void SendLinkBankAccountRequest(String username, String bankAccountId, String bankAccountOwnerName) {
        try{
            LinkBankAccountEvent event = LinkBankAccountEvent.builder()
                    .idOnTicsys(username)
                    .bankAccountId(bankAccountId)
                    .bankAccountOwnerName(bankAccountOwnerName)
                    .build();

            kafkaTemplate.send(linkBankAccountTopic, event);
            log.info("Sent LinkBankAccountRequestEvent to Kafka topic: " + linkBankAccountTopic + ", event: " + event);
        }
        catch (Exception e) {
            log.error("Failed to send LinkBankAccountRequestEvent to Kafka topic: " + linkBankAccountTopic + ", event: " + e.getMessage());
            throw e;
        }
    }

}
