package cn.tang.common.utils;

/**
 * @Auther: tangpd
 * @Date: 2019/1/11 15:47
 * @Description: 自定义异常类，可灵活闯入状态码，方便自己识别错误信息；
 */
public class ExceptionForMyCustom extends RuntimeException {

    //自定义异常状态码
    private Integer code;

    public ExceptionForMyCustom(ResultJsonEunm resultJsonEunm) {
        super(resultJsonEunm.getMsg());
        this.code = resultJsonEunm.getCode();
    }

    public ExceptionForMyCustom(ResultJsonEunm resultJsonEunm, String msg) {
        super(resultJsonEunm.getMsg() + "-->()" + msg);
        this.code = resultJsonEunm.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
