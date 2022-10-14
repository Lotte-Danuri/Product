package com.lotte.danuri.product.exception;

import com.lotte.danuri.product.error.ErrorCode;
import lombok.Getter;

@Getter
public class CategoryWasDeletedException extends RuntimeException{

    private ErrorCode errorCode;
    public CategoryWasDeletedException(String msg, ErrorCode errorCode){
        super(msg);
        this.errorCode = errorCode;
    }
}
