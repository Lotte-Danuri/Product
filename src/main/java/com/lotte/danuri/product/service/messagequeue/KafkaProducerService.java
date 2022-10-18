package com.lotte.danuri.product.service.messagequeue;

import com.lotte.danuri.product.model.dto.CouponDto;

public interface KafkaProducerService {
    CouponDto send(String topic, CouponDto couponDto);
}
