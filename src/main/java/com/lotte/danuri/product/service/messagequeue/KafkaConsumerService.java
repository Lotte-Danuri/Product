package com.lotte.danuri.product.service.messagequeue;

public interface KafkaConsumerService {
    void updateQty(String kafkaMessage);
}
