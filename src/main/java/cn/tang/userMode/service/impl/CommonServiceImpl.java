package cn.tang.userMode.service.impl;

import cn.tang.common.utils.*;
import cn.tang.userMode.service.CommonService;
import cn.tang.userMode.vo.CommonVo;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Auther: tangpd
 * @Date: 2020/12/29 16:34
 * @Description:
 */
@Service
public class CommonServiceImpl implements CommonService {


    /**
     *
     * 功能描述: 校验手机验证码
     *
     * @param: [request, commonVo]
     * @return: void
     * @auther: tangpd
     * @date: 2020/12/30 18:14
     */
    @Override
    public ResultJsonBean checkSMSCode(HttpServletRequest request, CommonVo commonVo) {
        String userIp = HttpClientUtil.getIp(request);//获取 ip 用做存储的 key
        boolean checkSMSCode = CheckBySMSUtil.checkSMSCode("sms_code_" + userIp + "_" + commonVo.getPhoneNum(), commonVo.getSmsCode());
        if (!checkSMSCode){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_PARAM_ERROR, "手机验证码错误");
        }
        return ResultUtil.getSuccessResultJsonBean("手机验证码校验成功");
    }

    /**
     *
     * 功能描述: 获取手机验证码，需要先验证图片验证码
     *
     * @param: [request, commonVo]
     * @return: void
     * @auther: tangpd
     * @date: 2020/12/30 11:56
     */
    @Override
    public ResultJsonBean getSMSCode(HttpServletRequest request, CommonVo commonVo) {
        //1、先校验图片验证码
        String userIp = HttpClientUtil.getIp(request);//获取 ip 用做存储 key 的一部分
        boolean checkImgCode = CheckByImgUtil.checkImgCode("img_code_" + userIp, commonVo.getImgCode());
        if (!checkImgCode){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_PARAM_ERROR, "图片验证码错误");
        }

        //2、发送短信验证码
        String phone = commonVo.getPhoneNum();
        boolean sendSMS = CheckBySMSUtil.sendSMS("sms_code_" + userIp + "_" + phone, phone);
        if (!sendSMS){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_SEND_SMS_ERROR, "发送短信失败");
        }

        return ResultUtil.getSuccessResultJsonBean("发送成功");
    }

    /**
     *
     * 功能描述: 获取图片验证码
     *
     * @param: [request, response]
     * @return: void
     * @auther: tangpd
     * @date: 2020/12/29 18:01
     */
    @Override
    public void getImgCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //1、获取相对应的资源
        String userIp = HttpClientUtil.getIp(request);//获取 ip 用做存储 key 的一部分
        String code = StringUtil.getRandomStringByNum(4, StringUtil.RANDOM_STR);
        BufferedImage img = CheckByImgUtil.generateImgCode(code);

        //2、响应用户端，返回图片资源
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/png");
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(img, "PNG", out);

        //3、释放资源
        img = null;
        out.close();

        //4、记录生成的验证码
        CheckByImgUtil.saveImgCodeInServer("img_code_" + userIp, code, 60);
    }
}
