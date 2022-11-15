package com.lotte.danuri.product.controller.coupon;

import com.lotte.danuri.product.model.dto.CouponDto;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.request.CouponListDto;
import com.lotte.danuri.product.model.dto.response.CouponByStoreDto;
import com.lotte.danuri.product.model.entity.Coupon;
import com.lotte.danuri.product.service.coupon.CouponService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "쿠폰 등록", notes = "쿠폰을 생성한다.")
    public ResponseEntity createCoupon (@RequestBody CouponDto couponDto){

        Long couponId = couponService.createCoupon(couponDto);
        return ResponseEntity.ok(couponId);
    }

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "쿠폰 조회", notes = "모든 쿠폰을 조회한다.")
    public ResponseEntity<?> getCoupons(){

        List<CouponDto> couponList = couponService.getCoupons();
        return ResponseEntity.ok(couponList);
    }

    @DeleteMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "쿠폰 삭제", notes = "하나의 쿠폰을 삭제한다.")
    public ResponseEntity deleteCoupon(@RequestBody CouponDto couponDto){

        couponService.deleteCoupon(couponDto.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "쿠폰 수정", notes = "하나의 쿠폰을 수정한다.")
    public ResponseEntity updateCoupon(@RequestBody CouponDto couponDto){

        couponService.updateCoupon(couponDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/list", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "쿠폰 리스트 조회", notes = "쿠폰 ID 리스트에 의해 쿠폰 리스트를 조회한다.")
    public ResponseEntity getCouponList (@RequestBody CouponListDto couponListDto){

        List<CouponDto> couponList = couponService.getCouponList(couponListDto);
        return ResponseEntity.ok(couponList);
    }

    @GetMapping(value = "/{storeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "스토어 쿠폰 조회", notes = "스토어 Id에 의해 모든 쿠폰을 조회한다.")
    public ResponseEntity<?> getCouponsByStoreId(@PathVariable("storeId") Long storeId){

        List<CouponByStoreDto> couponList = couponService.getCouponsByStoreId(storeId);
        return ResponseEntity.ok(couponList);
    }
}
