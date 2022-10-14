package com.lotte.danuri.product.exception;

import com.lotte.danuri.product.error.ErrorCode;
import lombok.Getter;

@Getter
public class ProductWasDeletedException extends RuntimeException{

    private ErrorCode errorCode;
    public ProductWasDeletedException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
