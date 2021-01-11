package cn.tang.common.utils;

import cn.tang.common.jwt.JwtUtil;

import java.io.File;
import java.util.*;

import static cn.tang.common.jwt.JwtUtil.*;

/**
 * @Auther: tangpd
 * @Date: 2020/12/2 18:37
 * @Description:
 */
public class TestUtil {


    private static void testAES(){

/*
        String key = "qy&M*4";
        String str = "123123";
        System.out.println("md5******************()" + DataMD5Util.md5Str(key, str));
*/

        //有效期
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 3);

        //用户相关信息
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "sdffs5d4f");
        userInfo.put("userRole", "admin;user;XXX");
        userInfo.put("userPe", "create;update;delete");

        //1、获取到的 JWT token
        String token = getJWTToken(userInfo, instance.getTime());

        String token1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyUGVybWlzc2lvbnMiOiJhbGw7dXNlcjpzZWxlY3Q6Kjt1c2VyOnVwZGF0ZToyMjtnZXRVc2VyUm9sZTt1cGRhdGVVc2VyO2FsbDE7YWxsMjthZGRVc2VyO2RlbGV0ZVVzZXIiLCJ1c2VyUm9sZSI6InVzZXI7YWRtaW4iLCJleHAiOjE2MDc3NDM4MjksInVzZXJJZCI6ImVlZGIxNGUxODYxNjQ2MjZhYTMwYzk0ZWE1ZGU1NWJiIn0.Kqvv-OvkwsdpbTMbu3JTMDCmxdr2mQLqikwdJQnzmwI";

        System.out.println("***********************(1)" + (token1.split(".").length));
        System.out.println("***********************(2)" + JwtUtil.validationToken(token1));

        System.out.println("获取到的 JWT token*******************()" + token1);

        //2、获取信息
        String userId = getInfoByTokenAndName(token1, "userId");
        String userRole = getInfoByTokenAndName(token1, "userRole");
        System.out.println("获取到的 userId*******************()" + userId);
        System.out.println("获取到的 userRole*******************()" + userRole);
    }



    public static void main(String[] args) {

        String f0 = "36b3330b471eb5a257a93e45cc3b37f7";
        String f1 = "36b3330b471eb5a257a93e45cc3b37f7";
        String f2 = "36b3330b471eb5a257a93e45cc3b37f7" + File.separator;

        System.out.println("0-1******************()" + f0.equals(f1));
        System.out.println("0-1******************()" + f2);


    }




}
