package com.lotte.danuri.product.exception;

import com.lotte.danuri.product.error.ErrorCode;
import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException{

    private ErrorCode errorCode;
    public ProductNotFoundException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
