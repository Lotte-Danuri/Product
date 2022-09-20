package com.lotte.danuri.product.controller.coupon;

import com.lotte.danuri.product.model.dto.CouponDto;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.Coupon;
import com.lotte.danuri.product.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/coupons")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CouponController {

    private final CouponService couponService;

    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createCoupon (@RequestBody CouponDto couponDto){

        couponService.createCoupon(couponDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getCoupons(){

        List<CouponDto> couponList = couponService.getCoupons();
        return ResponseEntity.ok(couponList);
    }

    @DeleteMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity deleteCoupon(@RequestBody CouponDto couponDto){

        couponService.deleteCoupon(couponDto.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCoupon(@RequestBody CouponDto couponDto){

        couponService.updateCoupon(couponDto);
        return ResponseEntity.ok().build();
    }
}
