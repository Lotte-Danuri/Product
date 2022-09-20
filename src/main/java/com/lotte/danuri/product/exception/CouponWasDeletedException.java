package com.lotte.danuri.product.exception;

import com.lotte.danuri.product.error.ErrorCode;
import lombok.Getter;

@Getter
public class CouponWasDeletedException extends RuntimeException{

    private ErrorCode errorCode;
    public CouponWasDeletedException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
