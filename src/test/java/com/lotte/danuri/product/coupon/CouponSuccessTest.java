package com.lotte.danuri.product.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotte.danuri.product.model.dto.CouponDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CouponSuccessTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/admin/coupons";
    private static final Long id = 14L;
    private static final Long storeId = 1L;
    private static final String name = "구찌 강남점 쿠폰";
    private static final String contents = "구찌 강남점에서 전체 15% 쿠폰을!";
    private static final LocalDateTime startDate = LocalDateTime.parse("2021-11-08T11:44:30");
    private static final LocalDateTime endDate = LocalDateTime.parse("2022-09-13T12:30:13");
    private static final Double discountRate = 15D;
    private static final Double minOrderPrice = 5000D;
    private static final Double maxDiscountPrice = 10000D;

    @Test
    @DisplayName("쿠폰 생성 테스트")
    void create_coupon_test() throws Exception {

        List<Long> productId = new ArrayList<>();
        productId.add(44L);
        productId.add(45L);
        productId.add(46L);

        String body = mapper.writeValueAsString(
                CouponDto.builder()
                        .storeId(storeId)
                        .name(name)
                        .contents(contents)
                        .startDate(startDate)
                        .endDate(endDate)
                        .discountRate(discountRate)
                        .minOrderPrice(minOrderPrice)
                        .maxDiscountPrice(maxDiscountPrice)
                        .productId(productId)
                        .build()
        );

        mvc.perform(post(BASE_URL)
                        .header("memberId", "1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("쿠폰 조회 테스트")
    void read_coupon_test() throws Exception {

        mvc.perform(get(BASE_URL)
                        .header("memberId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("쿠폰 수정 테스트")
    void update_coupon_test() throws Exception {

        List<Long> productId = new ArrayList<>();
        productId.add(45L);
        productId.add(46L);
        productId.add(47L);

        String body = mapper.writeValueAsString(
                CouponDto.builder()
                        .id(id)
                        .storeId(storeId)
                        .name(name)
                        .contents(contents)
                        .startDate(startDate)
                        .endDate(endDate)
                        .discountRate(discountRate)
                        .minOrderPrice(minOrderPrice)
                        .maxDiscountPrice(maxDiscountPrice)
                        .productId(productId)
                        .build()
        );

        mvc.perform(patch(BASE_URL)
                        .header("memberId", "1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("쿠폰 삭제 테스트")
    void delete_coupon_test() throws Exception {

        String body = mapper.writeValueAsString(
                CouponDto.builder()
                        .id(id)
                        .build()
        );

        mvc.perform(delete(BASE_URL)
                        .header("memberId", "1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}