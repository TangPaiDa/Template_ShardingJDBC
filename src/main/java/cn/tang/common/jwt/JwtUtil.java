package cn.tang.common.jwt;

import cn.tang.common.utils.ExceptionForMyCustom;
import cn.tang.common.utils.ResultJsonEunm;
import cn.tang.common.utils.StringUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * @Auther: tangpd
 * @Date: 2020/12/11 10:39
 * @Description: 封装 JWT 相关方法
 */
@Slf4j
public class JwtUtil {

    private static String secretKey = "sdsf&^df76-+*414sdf";


    //3、验证 token 的有效性，返回 true 说明有效
    public static boolean validationToken(String token){
        try {
            JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        }catch (TokenExpiredException e) {
            //decodedJWT.getExpiresAt();获取过期时间
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_NOT_LOGIN_ERROR, "登录信息已过期，请重新登录");
        } catch (SignatureVerificationException e) {
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_TOKEN_ERROR, "登录令牌信息可能被修改，请检查或者重新登录");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    //2、获取 token 中指定的信息
    public static String getInfoByTokenAndName(String token, String name){
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        } catch (TokenExpiredException e) {
            //decodedJWT.getExpiresAt();获取过期时间
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_NOT_LOGIN_ERROR, "登录信息已过期，请重新登录");
        } catch (SignatureVerificationException e) {
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_TOKEN_ERROR, "登录令牌信息可能被修改，请检查或者重新登录");
        } catch (Exception e) {
            throw e;
        }
        return decodedJWT.getClaim(name).asString();
    }

    //1、获取 JWT Token
    public static String getJWTToken(Map<String, String> payloadMap, Date validityDate){
        //1、创建 JWT builder
        JWTCreator.Builder builder = JWT.create();
        /*
        2、设置：
            header 头部；（对后台没什么用，加解密使用一致即可）
            payload 用户相关信息
            expiresAt 设置有效期时间
         */
        payloadMap.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        builder.withExpiresAt(validityDate);
        //3、获取签名
        return builder.sign(Algorithm.HMAC256(secretKey));
    }

    //TODO 设置秘钥，每天重置一次，放置于缓存中，不要明文可见。

}
