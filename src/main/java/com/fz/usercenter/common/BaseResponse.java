package com.fz.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author fang
 * @Date 2025/2/7 10:31
 * @注释  通用返回类
 */

@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -5516655821654757600L;
    private int code;
    private T data;
    private String msg;
    private String description;
    public BaseResponse(int code, T data, String msg,String description) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }

}
