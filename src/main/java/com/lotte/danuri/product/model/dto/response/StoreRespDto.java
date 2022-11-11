package com.lotte.danuri.product.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class StoreRespDto {

    private Long storeId;
    private String storeName;

}
