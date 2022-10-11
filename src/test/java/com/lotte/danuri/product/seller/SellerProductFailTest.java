package com.lotte.danuri.product.seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotte.danuri.product.model.dto.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SellerProductFailTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/sellers/products";
    private Long id = 1000L;
    private static final String productName = "가디건";
    private static final Double price = 1000800D;
    private static final Long stock = 100L;
    private static final Long storeId = 1L;
    private static final String productCode = "123456789";
    private static final Long warranty = 24L;
    private static final Long categoryFirstId = 3L;
    private static final Long categorySecondId = 3L;
    private static final Long categoryThirdId = 100L;

    @Test
    @DisplayName("상품 등록/수정 실패 테스트 (카테고리 미존재)")
    void create_product_fail_test_by_no_category() throws Exception {

        URL resource1 = getClass().getResource("/img/test1.jpg");
        MockMultipartFile multipartFile1 = new MockMultipartFile(
                "imageList",
                "test1.jpg",
                "image/jpeg",
                new FileInputStream(resource1.getFile()));

        URL resource2 = getClass().getResource("/img/test2.jpg");
        MockMultipartFile multipartFile2 = new MockMultipartFile(
                "imageList",
                "test2.jpg",
                "image/jpeg",
                new FileInputStream(resource2.getFile()));

        String body = mapper.writeValueAsString(
                ProductDto.builder()
                        .categoryFirstId(categoryFirstId)
                        .categorySecondId(categorySecondId)
                        .categoryThirdId(categoryThirdId)
                        .productName(productName)
                        .stock(stock)
                        .price(price)
                        .productCode(productCode)
                        .storeId(storeId)
                        .warranty(warranty)
                        .build()
        );

        MockMultipartFile product = new MockMultipartFile(
                "productDto",
                "productDto",
                "application/json",
                body.getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(BASE_URL)
                        .file(multipartFile1)
                        .file(multipartFile2)
                        .file(product)
                        .header("memberId", "1")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 수정/삭제 실패 테스트 (상품 미존재)")
    void delete_product_fail_test_by_no_product() throws Exception {

        String body = mapper.writeValueAsString(
                ProductDto.builder()
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
    @DisplayName("상품 수정/삭제 실패 테스트 (상품 삭제)")
    void delete_product_fail_test_by_delete_product() throws Exception {

        id = 1000L;

        String body = mapper.writeValueAsString(
                ProductDto.builder()
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
