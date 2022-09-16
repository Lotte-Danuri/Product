package com.lotte.danuri.product.controller.coupon;

import com.lotte.danuri.product.model.dto.CouponDto;
import com.lotte.danuri.product.model.entity.Coupon;
import com.lotte.danuri.product.model.request.CouponRequest;
import com.lotte.danuri.product.model.response.CouponResponse;
import com.lotte.danuri.product.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    @PostMapping("")
    public ResponseEntity<CouponResponse> createCoupon (@RequestBody CouponRequest request){

        CouponDto couponDto = new ModelMapper().map(request, CouponDto.class);

        CouponDto createCoupon = couponService.createCoupon(couponDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<CouponResponse>> getCoupons(){

        Iterable<Coupon> couponList = couponService.getAllCoupons();

        List<CouponResponse> result = new ArrayList<>();
        couponList.forEach(v -> {
            result.add(new ModelMapper().map(v, CouponResponse.class));
        });

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("")
    public ResponseEntity<CouponResponse> deleteCoupon(@RequestBody CouponRequest request){

        CouponDto couponDto = new ModelMapper().map(request, CouponDto.class);

        couponService.deleteCoupon(couponDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("")
    public ResponseEntity<CouponResponse> updateCoupon(@RequestBody CouponRequest request){

        CouponDto couponDto = new ModelMapper().map(request, CouponDto.class);

        couponService.updateCoupon(couponDto);

        return ResponseEntity.ok().build();
    }
}
