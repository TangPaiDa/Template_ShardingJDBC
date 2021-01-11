package cn.tang.common.utils;

/**
 * @Auther: tangpd
 * @Date: 2019/1/11 10:41
 * @Description: 返回的json格式的JavaBean
 */
public class ResultJsonBean {

    //返回的状态码
    private Integer code;

    //返回的提示信息
    private String msg;

    //返回的具体内容
    private Object data;

    /**
     *
     * 功能描述: 成功构造，该构造方法提供成功之后调用，只要传入具体内容信息即可
     *
     * @param: [object] 具体内容信息
     * @return: 返回结果的json
     * @auther: tangpd
     * @date: 2019/1/11 10:51
     */
    public ResultJsonBean(Object object) {
        this.code = ResultJsonEunm.SUCCESS_EUNM.getCode();
        this.msg = ResultJsonEunm.SUCCESS_EUNM.getMsg();
        this.data = object;
    }
    /**
     *
     * 功能描述: 异常构造，该构造方法提供失败之后调用，传入状态码、错误信息即可
     *
     * @param: [code, msg]
     * @return:
     * @auther: tangpd
     * @date: 2019/1/11 10:54
     */
    public ResultJsonBean(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = "The request is error, so response is null";
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object object) {
        this.data = object;
    }

    @Override
    public String toString() {
        return "ResultJsonBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", object=" + data +
                '}';
    }
}
