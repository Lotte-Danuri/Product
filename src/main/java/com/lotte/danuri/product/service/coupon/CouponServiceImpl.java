package com.lotte.danuri.product.service.coupon;

import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.CouponNotFoundException;
import com.lotte.danuri.product.exception.CouponWasDeletedException;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.exception.ProductWasDeletedException;
import com.lotte.danuri.product.model.dto.CouponDto;
import com.lotte.danuri.product.model.dto.CouponProductDto;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.request.CouponListDto;
import com.lotte.danuri.product.model.dto.response.CouponByStoreDto;
import com.lotte.danuri.product.model.entity.Coupon;
import com.lotte.danuri.product.model.entity.CouponProduct;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.CouponProductRepository;
import com.lotte.danuri.product.repository.CouponRepository;
import com.lotte.danuri.product.repository.ProductRepository;
import com.lotte.danuri.product.service.messagequeue.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final CouponProductRepository couponProductRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public void createCoupon(CouponDto couponDto) {
        //스토어 ID 예외처리 추가해야 함.

        // 예외처리
        // 1. 상품이 DB에 없는 경우
        // 2. 상품이 삭제된 경우
        couponDto.getProductId().forEach(v -> {
            Optional<Product> product = productRepository.findById(v);
            if(product.isEmpty()){
                throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
            }
            if(product.get().getDeletedDate() != null){
                throw new ProductWasDeletedException("Product was deleted in the database", ErrorCode.PRODUCT_WAS_DELETED);
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

        kafkaProducerService.send("coupon-insert", CouponDto.builder()
                .id(coupon.getId())
                .storeId(coupon.getStoreId())
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
    public List<CouponDto> getCoupons(){
        log.info("Before Retrieve [getCoupons] Method IN [Product-Service]");
        List<Coupon> coupons = couponRepository.findAll();
        List<CouponDto> result = new ArrayList<>();

        coupons.forEach(v -> {
            List<Long> productId = new ArrayList<>();
            v.getCouponProducts().forEach(w -> {
                productId.add(w.getId());
            });
            CouponDto couponDto = new CouponDto(v,productId);
            result.add(couponDto);
        });
        log.info("After Retrieve [getCoupons] Method IN [Product-Service]");
        return result;
    }

    @Override
    public void deleteCoupon(Long id) {
        Optional<Coupon> coupon = couponRepository.findById(id);

        // 예외처리
        // 1. 쿠폰이 DB에 없는 경우
        if(coupon.isEmpty()){
            throw new CouponNotFoundException("Coupon not present in the database", ErrorCode.COUPON_NOT_FOUND);
        }

        // 2. 쿠폰이 삭제된 경우
        if(coupon.get().getDeletedDate() != null){
            throw new CouponWasDeletedException("Coupon was deleted in the database", ErrorCode.COUPON_WAS_DELETED);
        }

        // 쿠폰 DELETE
        coupon.get().updateDeleteDate(LocalDateTime.now());
        couponRepository.save(coupon.get());

        // 쿠폰에 적용된 상품 DELETE
        coupon.get().getCouponProducts().forEach(v -> {
            v.updateDeleteDate(LocalDateTime.now());
        });
        couponProductRepository.saveAll(coupon.get().getCouponProducts());
    }

    @Override
    public void updateCoupon(CouponDto couponDto) {
        Optional<Coupon> coupon = couponRepository.findById(couponDto.getId());

        // 예외처리
        // 1. 쿠폰이 DB에 없는 경우
        if(coupon.isEmpty()){
            throw new CouponNotFoundException("Coupon not present in the database", ErrorCode.COUPON_NOT_FOUND);
        }

        // 2. 쿠폰이 삭제된 경우
        if(coupon.get().getDeletedDate() != null){
            throw new CouponWasDeletedException("Coupon was deleted in the database", ErrorCode.COUPON_WAS_DELETED);
        }

        // 3. 상품이 DB에 없는 경우
        // 4. 상품이 삭제된 경우
        couponDto.getProductId().forEach(v -> {
            Optional<Product> product = productRepository.findById(v);
            if(product.isEmpty()){
                throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
            }
            if(product.get().getDeletedDate() != null){
                throw new ProductWasDeletedException("Product was deleted in the database", ErrorCode.PRODUCT_WAS_DELETED);
            }
        });

        // 쿠폰 UPDATE
        coupon.get().update(couponDto);
        couponRepository.save(coupon.get());

        // 쿠폰에 적용된 상품 UPDATE - 먼저 삭제
        List<CouponProduct> couponProductList = new ArrayList<>();

        coupon.get().getCouponProducts().forEach(v -> {
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

    @Override
    public List<CouponDto> getCouponList(CouponListDto couponListDto){
        List<Coupon> coupons = couponRepository.findAllByIdIn(couponListDto.getCouponId());

        List<CouponDto> result = new ArrayList<>();

        coupons.forEach(v -> {
            CouponDto couponDto = new CouponDto(v);
            result.add(couponDto);
        });

        return result;
    }

    @Override
    public List<CouponByStoreDto> getCouponsByStoreId(Long storeId){
        List<Coupon> coupons = couponRepository.findAllByStoreId(storeId);
        List<CouponByStoreDto> result = new ArrayList<>();

        coupons.forEach(v -> {
            List<ProductDto> productDtoList = new ArrayList<>();
            v.getCouponProducts().forEach(w -> {
                ProductDto productDto = new ProductDto(productRepository.findByIdAndDeletedDateIsNull(w.getProduct().getId()));
                productDtoList.add(productDto);
            });
            CouponByStoreDto couponByStoreDto = new CouponByStoreDto(v,productDtoList);
            result.add(couponByStoreDto);
        });

        return result;
    }

    @Override
    public List<CouponDto> getCouponDetailList(CouponListDto couponListDto){
        List<CouponDto> couponDtoList = new ArrayList<>();

        List<CouponProduct> couponProductList = couponProductRepository.findAllByProductIdAndCouponIdInAndDeletedDateIsNull(
                couponListDto.getProductId(),
                couponListDto.getCouponId()
        );

        couponProductList.forEach(v ->
                couponDtoList.add(new CouponDto(v.getCoupon()))
        );

        return couponDtoList;
    }

    @Override
    public CouponByStoreDto getCouponDetail(Long couponId){
        Optional<Coupon> coupon = couponRepository.findById(couponId);

        // 예외처리
        // 1. 쿠폰이 DB에 없는 경우
        if(coupon.isEmpty()){
            throw new CouponNotFoundException("Coupon not present in the database", ErrorCode.COUPON_NOT_FOUND);
        }

        // 2. 쿠폰이 삭제된 경우
        if(coupon.get().getDeletedDate() != null){
            throw new CouponWasDeletedException("Coupon was deleted in the database", ErrorCode.COUPON_WAS_DELETED);
        }

        List<ProductDto> productDtoList = new ArrayList<>();
        coupon.get().getCouponProducts().forEach(v -> {
            productDtoList.add(new ProductDto(v.getProduct()));
        });
        CouponByStoreDto couponDto = new CouponByStoreDto(coupon.get(),productDtoList);
        return couponDto;
    }
}