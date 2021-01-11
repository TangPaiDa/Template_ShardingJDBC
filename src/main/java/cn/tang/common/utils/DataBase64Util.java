package cn.tang.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @Auther: tangpd
 * @Date: 2020/12/11 14:08
 * @Description:
 */
public class DataBase64Util {

    private static final DataBase64Util DATA_BASE_64_UTIL = new DataBase64Util();
    private DataBase64Util() {}
    public static DataBase64Util getDataBase64Util() {return DATA_BASE_64_UTIL;}

    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     *
     * 功能描述: 对字符串进行编码并返回编码之后的字符串
     *
     * @param: [str]
     * @return: java.lang.String
     * @auther: tangpd
     * @date: 2020/12/11 14:17
     */
    public static String encoderStr(String str) {
        byte[] textByte = new byte[0];
        try {
            textByte = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_PARAM_BASE64_ERROR);
        }
        return encoder.encodeToString(textByte);
    }

    /**
     *
     * 功能描述: 对编码之后的字符串进行解码，返回原数据
     *
     * @param: [str]
     * @return: java.lang.String
     * @auther: tangpd
     * @date: 2020/12/11 14:18
     */
    public static String decodeStr(String str) {
        String decodeStr = null;
        try {
            decodeStr = new String(decoder.decode(str), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_PARAM_BASE64_ERROR);
        }
        return decodeStr;
    }

}
