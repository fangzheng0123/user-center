package com.fz.usercenter.common;

/**
 * @Author fang
 * @Date 2025/2/7 10:34
 * @注释  封装的统一返回值
 */
public class ResultUtils {

    //    成功返回值
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok","");
    }

    //    失败返回消息
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     *
     * @param errorCode 错误信息
     * @param msg  错误消息
     * @param description 错误详细信息
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String msg,String description) {
        return new BaseResponse<>(errorCode.getCode(),null,msg,description);
    }

    /**
     *
     * @param errorCode 错误信息
     * @param description 错误详细信息
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String description) {
        return new BaseResponse<>(errorCode.getCode(),null,"",description);
    }

    /**
     *
     * @param code 传递的code编码信息
     * @param description 错误详细信息
     * @return
     */
    public static BaseResponse error(int code,String msg,String description) {
        return new BaseResponse<>(code,null,msg,description);
    }
}
