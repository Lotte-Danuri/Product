package com.lotte.danuri.product.buyer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BuyerProductFailTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/products";

    @Test
    @DisplayName("상품 상세 조회 실패 테스트 (상품 미존재)")
    void read_product_detail_fail_test_by_no_product() throws Exception {
        Long id = 1000L;
        mvc.perform(get(BASE_URL + "/" + id)
                        .header("memberId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 상세 조회 실패 테스트 (상품 삭제)")
    void read_product_detail_fail_test_by_product_delete() throws Exception {
        Long id = 43L;
        mvc.perform(get(BASE_URL + "/" + id)
                        .header("memberId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}
