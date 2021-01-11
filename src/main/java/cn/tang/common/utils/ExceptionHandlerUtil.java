package cn.tang.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: tangpd
 * @Date: 2019/1/11 13:38
 * @Description: 统一处理异常类，使用try catch 结合ResultJsonBean 也是可以达到同样的处理效果，
 *               但是try catch多了会发现代码冗余严重，不如统一抛出，再到这里处理来得简洁；
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandlerUtil {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultJsonBean handleException(Exception e){
        if (e instanceof ExceptionForMyCustom){
            //是自己抛出的异常
            ExceptionForMyCustom exceptionForMyCustom = (ExceptionForMyCustom) e;
            log.error("the request throw an Exception ->( {} ): {}", exceptionForMyCustom.getCode(), e.getMessage());
            //log.error("the request throw an Exception ->( {} ): {}", exceptionForMyCustom.getCode(), e.getMessage(), e);
            return ResultUtil.getErrorResultJsonBean(exceptionForMyCustom.getCode(), exceptionForMyCustom.getMessage());
        }else if (e instanceof HttpMessageNotReadableException){
            //用户端没有传参
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_ILLEGAL_ERROR, "请检查接口是否需要传参");
        }else if (e instanceof HttpMediaTypeNotSupportedException){
            //用户端传参格式不对
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_ILLEGAL_ERROR, "请检查接口传参类型 Content type 是否正确");
        }else if (e instanceof HttpRequestMethodNotSupportedException){
            //用户端接口形式不对
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_PARAM_ILLEGAL_ERROR, "请检查接口 GET、POST 类型");
        }else {
            //自己未知，程序运行时异常
            log.error("the request throw an Exception ->( {} ): ", e.getMessage(), e);
            return ResultUtil.getErrorResultJsonBean(ResultJsonEunm.ERROR_FOR_SYSTEM.getCode(), ResultJsonEunm.ERROR_FOR_SYSTEM.getMsg());
        }
    }
}
