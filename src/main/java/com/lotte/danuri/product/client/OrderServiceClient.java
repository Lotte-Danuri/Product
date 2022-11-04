package com.lotte.danuri.product.client;

import com.lotte.danuri.product.model.dto.request.ProductListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "order")
public interface OrderServiceClient {
    @PostMapping("/orders/pays/list/count")
    List<Long> getOrdersCount (@RequestBody ProductListDto productListDto);

    @PostMapping("/orders/pays/list/date/count")
    List<Long> getOrdersCountByDate(@RequestBody ProductListDto productListDto);
}
