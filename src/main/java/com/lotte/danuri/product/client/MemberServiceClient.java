package com.lotte.danuri.product.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "member")
public interface MemberServiceClient {

}
