package com.lotte.danuri.product.controller.coupon;

import com.lotte.danuri.product.model.dto.CouponDto;
import com.lotte.danuri.product.model.dto.request.CouponListDto;
import com.lotte.danuri.product.service.coupon.CouponService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/coupons")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BuyerCouponController {

    private final CouponService couponService;

    @PostMapping(value = "/list", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 적용 가능 쿠폰 리스트 조회", notes = "사용자가 상품에 적용 가능한 쿠폰 리스트를 조회한다.")
    public ResponseEntity getCouponDetailList (@RequestBody CouponListDto couponListDto){

        List<CouponDto> couponList = couponService.getCouponDetailList(couponListDto);
        return ResponseEntity.ok(couponList);
    }

}
