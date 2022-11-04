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
public class CouponFailTest {

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

    @Test
    @DisplayName("쿠폰 생성/수정 실패 테스트 (상품 미존재)")
    void create_or_update_coupon_fail_test_by_no_product() throws Exception {

        List<Long> productId = new ArrayList<>();
        productId.add(1000L);
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
                        .productId(productId)
                        .build()
        );

        mvc.perform(post(BASE_URL)
                        .header("memberId", "1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("쿠폰 생성/수정 실패 테스트 (상품 삭제)")
    void create_or_update_coupon_fail_test_by_product_delete() throws Exception {

        List<Long> productId = new ArrayList<>();
        productId.add(43L);
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
                        .productId(productId)
                        .build()
        );

        mvc.perform(post(BASE_URL)
                        .header("memberId", "1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("쿠폰 수정/삭제 실패 테스트 (쿠폰 미존재)")
    void update_or_delete_coupon_fail_test_by_no_coupon() throws Exception {

        Long id = 1000L;

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
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("쿠폰 수정/삭제 실패 테스트 (쿠폰 삭제)")
    void update_or_delete_coupon_fail_test_by_coupon_delete() throws Exception {

        Long id = 13L;

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
                .andExpect(status().isBadRequest());
    }
}
