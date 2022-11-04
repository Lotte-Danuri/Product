package com.lotte.danuri.product.client;

import com.lotte.danuri.product.model.dto.request.ProductListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "recommend")
public interface RecommendServiceClient {
    @PostMapping("/recommends/click/count")
    List<Long> getClickCount(@RequestBody ProductListDto productListDto);

    @PostMapping("/recommends//click/date/count")
    List<Long> getClickCountByDate(@RequestBody ProductListDto productListDto);
}
