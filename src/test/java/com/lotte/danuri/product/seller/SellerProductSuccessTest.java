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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SellerProductSuccessTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/sellers/products";
    private static final Long id = 44L;
    private static final String productName = "가디건";
    private static final Double price = 1000800D;
    private static final Long stock = 100L;
    private static final Long storeId = 1L;
    private static final String productCode = "123456789";
    private static final Long warranty = 24L;
    private static final Long categoryFirstId = 3L;
    private static final Long categorySecondId = 3L;
    private static final Long categoryThirdId = 3L;

    /*
    @Test
    @DisplayName("상품 등록 테스트")
    void create_product_test() throws Exception {

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
                .andExpect(status().isOk());
    }
    */

    @Test
    @DisplayName("상품 조회 테스트")
    void read_product_test() throws Exception {

        mvc.perform(get(BASE_URL)
                        .header("memberId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void delete_product_test() throws Exception {

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
                .andExpect(status().isOk());
    }
}