package com.lotte.danuri.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "member")
public interface MemberServiceClient {

    @GetMapping("/store/name/{storeId}")
    String getNames(@PathVariable Long storeId);

    @GetMapping("/store/{brandId}")
    List<Long> getStoreId(@PathVariable Long brandId);
}