package cn.tang.common.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: tangpd
 * @Date: 2020/12/29 11:23
 * @Description:
 */
public class CheckBySMSUtil {


    /**
     *
     * 功能描述: 检验手机验证码是否正确
     *
     * @param: [userKey, oldValue]
     * @return: boolean
     * @auther: tangpd
     * @date: 2020/12/30 18:10
     */
    public static boolean checkSMSCode(String userKey, String oldValue){
        Map<String, Object> codeInfoMap = getSMSCodeInServer(userKey);
        if (null == codeInfoMap){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_GET_CACHE_DATA_ERROR, "获取不到保存的手机验证码");
        }
        String smsCodeInServer = (String) codeInfoMap.get("code");
        return smsCodeInServer.equals(oldValue);
    }


    /**
     *
     * 功能描述: 外部调用发送短信接口，需要先判断该用户是否还可以发送短信验证码，避免恶意发送造成资源浪费
     *
     * @param: [userKey]
     * @return: boolean
     * @auther: tangpd
     * @date: 2020/12/30 14:19
     */
    public static boolean sendSMS(String userKey, String phone){
        Map<String, Object> codeInfoMap = getSMSCodeInServer(userKey);
        if (null == codeInfoMap){
            //第一次获取，直接调用方法发送短信
            return sendCodeToUser(userKey, StringUtil.getRandomStringByNum(6, StringUtil.RANDOM_NUM), phone);
        }
        //判断是否超过总数
        int allNum = (int) codeInfoMap.get("allNum");
        if (allNum >= 5){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_ALLNUM_ISMAX_ERROR, "当前用户获取短信数量已超过5次，暂停发送");//超过5条不给发送
        }
        //判断是否一分钟内获取
        LocalDateTime thisTime = LocalDateTime.now();
        LocalDateTime localDateTime = (LocalDateTime) codeInfoMap.get("time");
        Duration duration = Duration.between(localDateTime, thisTime);
        if (duration.toMinutes() < 60 * 1000){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_SEND_FREQUENT_ERROR, "一分钟内只能获取一次验证码，请稍后重试");//一分钟内只能发送一次
        }
        //验证通过，调用方法发送短信
        return sendCodeToUser(userKey, StringUtil.getRandomStringByNum(6, StringUtil.RANDOM_NUM), phone);
    }


    /**
     *
     * 功能描述: 这里从服务端获取发送给用户的手机验证码，注意 map 中需要包含的信息如下
     *
     *  code：发送的验证码；用于检验用户输入是否正确
     *  time：最后一次发送的时间；用于控制间隔发送时间（如一分钟只能发送一条）
     *  allNum：总共发送的次数；用于控制总数，例如控制一天不允许超过5次（需要结合缓存有效时间设置，例如 redis 设置 hash 的有效时间）
     *
     * @param: [userKey]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @auther: tangpd
     * @date: 2020/12/30 13:54
     */
    private static Map<String, Object> getSMSCodeInServer(String userKey){
        if (1 == 1){
            throw new RuntimeException("获取发送给用户的手机验证码，根据实际需求从 redis 获取或者从缓存中获取");
        }
        return new HashMap<>();
    }


    /**
     *
     * 功能描述: 发送短信给用户
     *
     * @param: [phone, code]
     * @return: boolean
     * @auther: tangpd
     * @date: 2020/12/30 13:42
     */
    private static boolean sendCodeToUser(String userKey, String code, String phone){
        if (1 == 1){
            throw new RuntimeException("发送短信给用户的实现，根据具体得平台规定来实现发送逻辑");
        }
        //发送成功之后保存数据
        saveSMSCodeInServer(userKey, code);
        return true;
    }


    /**
     *
     * 功能描述: 这里保存发送给用户的手机验证码到服务端，注意 map 中需要包含的信息如下
     *
     *  code：发送的验证码；用于检验用户输入是否正确
     *  time：最后一次发送的时间；用于控制间隔发送时间（如一分钟只能发送一条）
     *  allNum：总共发送的次数；用于控制总数，例如控制一天不允许超过5次（需要结合缓存有效时间设置，例如 redis 设置 hash 的有效时间）
     *
     * @param: [userKey, code]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @auther: tangpd
     * @date: 2020/12/30 14:17
     */
    private static Map<String, Object> saveSMSCodeInServer(String userKey, String code){
        Map<String, Object> codeInfoMap = getSMSCodeInServer(userKey);
        if (null == codeInfoMap){
            codeInfoMap = new HashMap<>();
            codeInfoMap.put("allNum", 1);
        }else {
            codeInfoMap.put("allNum", (int)codeInfoMap.get("allNum") + 1);
        }
        codeInfoMap.put("code", code);
        codeInfoMap.put("time", LocalDateTime.now());
        if (1 == 1){
            throw new RuntimeException("保存发送给用户的手机验证码，根据实际需求看是保存在 redis 还是缓存中");
        }
        return new HashMap<>();
    }


}
