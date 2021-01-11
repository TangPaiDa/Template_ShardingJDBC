package cn.tang.common.utils;

import javax.servlet.http.HttpServletRequest;

import static cn.tang.common.jwt.JwtUtil.getInfoByTokenAndName;

/**
 * @Auther: tangpd
 * @Date: 2020/12/2 15:40
 * @Description:
 */
public class SysUserInfoUtil {


    /**
     *
     * 功能描述: 获取当前登录者的用户id，如果没登录默认使用 system ；
     *
     * @param: []
     * @return: java.lang.String
     * @auther: tangpd
     * @date: 2020/12/2 15:44
     */
    public static String getSysUserId(HttpServletRequest request){
        String jwtToken = request.getHeader("Authorization");
        String userId = getInfoByTokenAndName(jwtToken, "userId");
        return StringUtil.is_empty(userId) ? "system" : userId;
    }
}
