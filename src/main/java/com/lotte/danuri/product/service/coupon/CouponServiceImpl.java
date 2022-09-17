package com.lotte.danuri.product.service.coupon;

import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.CouponNotFoundException;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.model.dto.CouponDto;
import com.lotte.danuri.product.model.dto.CouponProductDto;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.Coupon;
import com.lotte.danuri.product.model.entity.CouponProduct;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.CouponProductRepository;
import com.lotte.danuri.product.repository.CouponRepository;
import com.lotte.danuri.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final ProductRepository productRepository;

    private final CouponRepository couponRepository;
    private final CouponProductRepository couponProductRepository;
    @Override
    public void createCoupon(CouponDto couponDto) {
        //스토어 ID 예외처리 추가해야 함.

        couponDto.getProductId().forEach(v -> {
            if(productRepository.findById(v).isEmpty()){
                throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
            }
        });

        // 쿠폰 INSERT
        Coupon coupon = couponRepository.save(
                Coupon.builder()
                        .name(couponDto.getName())
                        .contents(couponDto.getContents())
                        .startDate(couponDto.getStartDate())
                        .endDate(couponDto.getEndDate())
                        .discountRate(couponDto.getDiscountRate())
                        .minOrderPrice(couponDto.getMinOrderPrice())
                        .maxDiscountPrice(couponDto.getMaxDiscountPrice())
                        .storeId(couponDto.getStoreId())
                        .build()
        );

        // 쿠폰에 적용된 상품 INSERT
        List<CouponProduct> couponProductList = new ArrayList<CouponProduct>();
        couponDto.getProductId().forEach(v -> {
            CouponProduct couponProduct = CouponProduct.builder()
                    .product(productRepository.findById(v).get())
                    .coupon(coupon)
                    .build();
            couponProductList.add(couponProduct);
        });
        couponProductRepository.saveAll(couponProductList);
    }

    @Override
    public List<CouponDto> getCoupons() {
         List<Coupon> coupons = couponRepository.findAll();
         List<CouponDto> result = new ArrayList<>();

         coupons.forEach(v -> {
             List<Long> couponProductId = new ArrayList<>();
             Optional<Iterable<CouponProduct>> couponProducts = couponProductRepository.findByCouponIdAndDeletedDateIsNull(v.getId());
             couponProducts.get().forEach(w -> {
                 couponProductId.add(w.getId());
             });

             CouponDto couponDto = new CouponDto(v);
             couponDto.updateProductId(couponProductId);

             result.add(couponDto);
         });
         return result;
    }

    @Override
    public void deleteCoupon(Long id) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if(coupon.isEmpty()){
            throw new CouponNotFoundException("Coupon not present in the database", ErrorCode.COUPON_NOT_FOUND);
        }

        // 쿠폰 DELETE
        coupon.get().updateDeleteDate(LocalDateTime.now());
        couponRepository.save(coupon.get());

        // 쿠폰에 적용된 상품 DELETE
        Optional<Iterable<CouponProduct>> optionalCouponProduct = couponProductRepository.findByCouponId(id);
        List<CouponProduct> couponProductList = new ArrayList<>();

        optionalCouponProduct.get().forEach(v -> {
            v.updateDeleteDate(LocalDateTime.now());
            couponProductList.add(v);
        });
        couponProductRepository.saveAll(couponProductList);
    }

    @Override
    public void updateCoupon(CouponDto couponDto) {
        Optional<Coupon> coupon = couponRepository.findById(couponDto.getId());
        if(coupon.isEmpty()){
            throw new CouponNotFoundException("Coupon not present in the database", ErrorCode.COUPON_NOT_FOUND);
        }

        couponDto.getProductId().forEach(v -> {
            if(productRepository.findById(v).isEmpty()){
                throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
            }
        });

        // 쿠폰 UPDATE
        coupon.get().update(couponDto);
        couponRepository.save(coupon.get());

        // 쿠폰에 적용된 상품 UPDATE - 먼저 삭제
        Optional<Iterable<CouponProduct>> optionalCouponProduct = couponProductRepository.findByCouponId(couponDto.getId());
        List<CouponProduct> couponProductList = new ArrayList<>();

        optionalCouponProduct.get().forEach(v -> {
            v.updateDeleteDate(LocalDateTime.now());
            couponProductList.add(v);
        });
        couponProductRepository.saveAll(couponProductList);

        // 쿠폰에 적용된 상품 UPDATE - 이제 추가
        List<CouponProduct> listCouponProduct = new ArrayList<CouponProduct>();
        couponDto.getProductId().forEach(v -> {
            CouponProduct couponProduct = CouponProduct.builder()
                    .product(productRepository.findById(v).get())
                    .coupon(coupon.get())
                    .build();
            listCouponProduct.add(couponProduct);
        });
        couponProductRepository.saveAll(listCouponProduct);

    }
}