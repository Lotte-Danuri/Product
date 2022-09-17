package com.lotte.danuri.product.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    CATEGORY_NOT_FOUND(400,"PRODUCT-ERR-400","CATEGORY NOT FOUND IN DB"),
    PRODUCT_NOT_FOUND(400,"PRODUCT-ERR-400","PRODUCT NOT FOUND IN DB"),
    COUPON_NOT_FOUND(400,"PRODUCT-ERR-400","COUPON NOT FOUND IN DB"),
    ;

    private int status;
    private String errorCode;
    private String message;
}
