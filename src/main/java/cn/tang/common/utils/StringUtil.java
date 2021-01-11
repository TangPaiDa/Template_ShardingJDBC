package cn.tang.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @Auther: tangpd
 * @Date: 2019/1/16 13:41
 * @Description:
 */
public class StringUtil {

    public static final String RANDOM_STR_$ = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
    public static final String RANDOM_STR = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final String RANDOM_NUM = "0123456789";


    /**
     *
     * 功能描述:获取指定位数的随机字符串
     *
     * @param: [n]
     * @return: java.lang.String
     * @auther: tangpd
     * @date: 2020/10/23 11:18
     */
    public static String getRandomStringByNum(Integer n, String randomString){
        if (null == n){
            n = 4;
        }
        if (is_empty(randomString)){
            randomString = RANDOM_STR_$;
        }
        StringBuffer sb = new StringBuffer();
        char[] chars = randomString.toCharArray();
        for (Integer i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }

    /**
     *
     * 功能描述: 判断字符串是否为空串
     *
     * @param: [str]
     * @return: boolean
     * @auther: tangpd
     * @date: 2020/10/23 11:19
     */
    public static boolean is_empty(String str){
        return null == str || "".equals(str) || str.trim().length() == 0;
    }

    /**
     *
     * 功能描述: 获取UUID；用于需要唯一标记的地方，如数据库的id。
     *
     * @param: []
     * @return: java.lang.String
     * @auther: tangpd
     * @date: 2019/1/16 13:42
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
