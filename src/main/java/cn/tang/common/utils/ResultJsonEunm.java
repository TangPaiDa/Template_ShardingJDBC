package cn.tang.common.utils;

/**
 * @Auther: tangpd
 * @Date: 2019/1/11 16:54
 * @Description: 使用枚举来统一管理状态码与提示信息，方便开发和维护；
 */
public enum ResultJsonEunm {

    //定义常量信息：状态码与提示信息；
    /*规则说明：
        由于常见的200是服务器响应浏览器成之后用的，3开头是重定向相关的，4开头是请求资源错误相关的，故此只使用0、1和5来做状态码；
    *       0开头表示成功；
    *       5开头为开发人员未知、系统运行时抛出、对外显示的状态，其信息要到控制台或日志文件中查找；
    *       1开头表示开发人员已知的、自己抛出的异常状态；
    *           101：开发测试使用的自定义异常抛出；
    *           1XX：...；
    *           ...
    *       ...
    *       */
    SUCCESS_EUNM(200, "Request is success and have a Response in Data！"),//正常成功响应

    ERROR_FOR_SYSTEM(5, "服务器异常，请联系管理员"),//服务器系统异常

    ERROR_FOR_PARAM_ISNULL(510, "存在空值参数"),//参数为空

    ERROR_FOR_PARAM_EXIST(511, "参数已存在"),//参数已存在

    ERROR_FOR_PARAM_ERROR(512, "参数不匹配"),//参数不匹配

    ERROR_FOR_PARAM_ILLEGAL_ERROR(513, "参数非法"),//参数非法

    ERROR_FOR_PARAM_IS_DISABLE(521, "数据禁用"),//数据禁用

    ERROR_FOR_PARAM_IS_DELETE(522, "数据删除"),//数据删除

    ERROR_FOR_PARAM_BASE64_ERROR(531, "数据进行Base64编解码异常"),//数据进行Base64编解码异常

    ERROR_FOR_PARAM_MD5_ERROR(532, "数据进行MD5加密常"),//数据进行MD5加密常

    ERROR_FOR_CACHE_DATA_ERROR(541, "缓存数据失败"),//缓存数据失败

    ERROR_FOR_GET_CACHE_DATA_ERROR(542, "获取缓存数据失败"),//获取缓存数据失败

    ERROR_FOR_ALLNUM_ISMAX_ERROR(543, "超过最大限制"),//超过最大限制

    ERROR_FOR_SEND_FREQUENT_ERROR(544, "操作过于频繁"),//操作过于频繁

    ERROR_FOR_SEND_SMS_ERROR(545, "发送短信失败"),//发送短信失败

    ERROR_FOR_DB_DML_ERROR(601, "数据库操作失败"),//数据库操作失败

    ERROR_FOR_DB_NOT_EXIST(611, "数据库不存在这条数据"),//数据库不存在这条数据

    ERROR_FOR_USER_NO_EXIST(700, "用户不存在"),//用户不存在

    ERROR_FOR_USER_PASSWORD_ERROR(701, "密码错误"),//密码错误

    ERROR_FOR_USER_NOT_LOGIN_ERROR(702, "用户未登录异常"),//用户未登录异常

    ERROR_FOR_USER_ROLE_ERROR(703, "用户身份权限异常"),//用户身份权限异常

    ERROR_FOR_USER_TOKEN_ERROR(704, "用户登录令牌无效"),//用户登录令牌无效

    ERROR_FOR_TEST(101, "测试异常信息");//测试异常

    //定义属性
    private Integer code;
    private String msg;

    //构造方法
    ResultJsonEunm(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //get方法
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
