package com.lotte.danuri.product.service.coupon;

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

    private final CouponRepository couponRepository;
    private final CouponProductRepository couponProductRepository;

    private final ProductRepository productRepository;

    public CouponDto createCoupon(CouponDto couponDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Coupon coupon = mapper.map(couponDto, Coupon.class);
        Coupon couponResult = couponRepository.save(coupon);

        List<CouponProduct> couponProductList = new ArrayList<CouponProduct>();
        couponDto.getProductId().forEach(v -> {
            CouponProduct couponProduct = new CouponProduct();
            couponProduct.setCoupon(couponResult);

            Optional<Product> product = productRepository.findById(v);
            couponProduct.setProduct(product.get());

            couponProductList.add(couponProduct);
        });

        couponProductRepository.saveAll(couponProductList);

        CouponDto returnValue = mapper.map(coupon, CouponDto.class);

        return returnValue;
    }

    public Iterable<Coupon> getAllCoupons(){
        return couponRepository.findAll();
    }

    public void deleteCoupon(CouponDto couponDto){
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponDto.getId());
        Optional<Iterable<CouponProduct>> optionalCouponProduct = couponProductRepository.findByCouponId(couponDto.getId());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        optionalCoupon.get().setDeletedDate(LocalDateTime.now());
        couponRepository.save(optionalCoupon.get());

        List<CouponProduct> couponProductList = new ArrayList<>();

        optionalCouponProduct.get().forEach(v -> {
            v.setDeletedDate(LocalDateTime.now());
            couponProductList.add(v);
        });

        couponProductRepository.saveAll(couponProductList);
    }

    public void updateCoupon(CouponDto couponDto){
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponDto.getId());
        Optional<Iterable<CouponProduct>> optionalCouponProduct = couponProductRepository.findByCouponId(couponDto.getId());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        optionalCoupon.get().setName(couponDto.getName());
        optionalCoupon.get().setContents(couponDto.getContents());
        optionalCoupon.get().setStartDate(couponDto.getStartDate());
        optionalCoupon.get().setEndDate(couponDto.getEndDate());
        optionalCoupon.get().setDiscountRate(couponDto.getDiscountRate());
        optionalCoupon.get().setMinOrderPrice(couponDto.getMinOrderPrice());
        optionalCoupon.get().setMaxDiscountPrice(couponDto.getMaxDiscountPrice());

        couponRepository.save(optionalCoupon.get());




        List<CouponProduct> couponProductList = new ArrayList<>();

        optionalCouponProduct.get().forEach(v -> {
            v.setDeletedDate(LocalDateTime.now());
            couponProductList.add(v);
        });

        couponProductRepository.saveAll(couponProductList);





        List<CouponProduct> listCouponProduct = new ArrayList<CouponProduct>();
        couponDto.getProductId().forEach(v -> {
            CouponProduct couponProduct = new CouponProduct();
            couponProduct.setCoupon(optionalCoupon.get());

            Optional<Product> product = productRepository.findById(v);
            couponProduct.setProduct(product.get());

            couponProductList.add(couponProduct);
        });

        couponProductRepository.saveAll(couponProductList);
    }
}