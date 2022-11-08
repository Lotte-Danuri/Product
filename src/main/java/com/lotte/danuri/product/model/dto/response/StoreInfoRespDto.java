package com.lotte.danuri.product.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreInfoRespDto {

    private Long storeId;
    private String storeName;
    private Long brandId;
    private String brandName;

}
