package cn.tang.common.utils;

import org.springframework.util.DigestUtils;

/**
 * @Auther: tangpd
 * @Date: 2020/12/11 14:29
 * @Description:
 */
public class DataMD5Util {

    public static  String  md5Str(String md5Key, String str){
        String base = str +"/"+md5Key;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
