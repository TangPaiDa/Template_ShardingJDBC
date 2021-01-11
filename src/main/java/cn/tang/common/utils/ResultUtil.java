package cn.tang.common.utils;

/**
 * @Auther: tangpd
 * @Date: 2019/1/11 11:15
 * @Description: 该类用于返回一个ResultJsonBean，使用方法里来new对象，可以通过方法名传递一些信息，
 *               而直接使用构造方法，则没有该优势；
 */
public class ResultUtil {

    public static ResultJsonBean getSuccessResultJsonBean(Object object){
        return new ResultJsonBean(object);
    }

    public static ResultJsonBean getErrorResultJsonBean(Integer code, String msg){
        return new ResultJsonBean(code, msg);
    }

    public static ResultJsonBean getErrorResultJsonBean(ResultJsonEunm resultJsonEunm){
        return new ResultJsonBean(resultJsonEunm.getCode(), resultJsonEunm.getMsg());
    }

    public static ResultJsonBean getErrorResultJsonBean(ResultJsonEunm resultJsonEunm, String msg){
        return new ResultJsonBean(resultJsonEunm.getCode(), resultJsonEunm.getMsg() + "-->()" + msg);
    }
}
