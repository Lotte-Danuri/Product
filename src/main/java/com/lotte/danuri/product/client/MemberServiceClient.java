package com.lotte.danuri.product.client;

import com.lotte.danuri.product.model.dto.request.MyCouponReqDto;
import com.lotte.danuri.product.model.dto.response.StoreInfoRespDto;
import com.lotte.danuri.product.model.dto.response.StoreRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "member")
public interface MemberServiceClient {

    @GetMapping("/store/name/{storeId}")
    StoreInfoRespDto getNames(@PathVariable Long storeId);

    @GetMapping("/store/{brandId}")
    List<Long> getStoreId(@PathVariable Long brandId);

    @PostMapping("/mycoupon/check")
    boolean check(@RequestBody MyCouponReqDto dto);

    @GetMapping("/store/stores/{brandId}")
    List<StoreRespDto> getStore(@PathVariable Long brandId);
}