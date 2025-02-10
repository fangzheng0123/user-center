package com.fz.usercenter.exception;

import com.fz.usercenter.common.BaseResponse;
import com.fz.usercenter.common.ErrorCode;
import com.fz.usercenter.common.ResultUtils;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author fang
 * @Date 2025/2/7 20:15
 * @注释  全局异常处理
 */

@SuppressWarnings({"all"})
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @ExceptionHandler(BusinessException.class) 注解的作用就是只捕获BusinessException
     * 中出现的异常信息
     * @param e 捕获的异常信息
     * @return 异常返回结果
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessException(BusinessException e){
        log.error("businessException"+e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }


    /**
     *  系统运行全局异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeException(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
