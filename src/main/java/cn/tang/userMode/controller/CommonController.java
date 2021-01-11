package cn.tang.userMode.controller;

import cn.tang.common.utils.ReflectionUtil;
import cn.tang.common.utils.ResultJsonBean;
import cn.tang.userMode.service.CommonService;
import cn.tang.userMode.vo.CommonVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Auther: tangpd
 * @Date: 2020/12/29 16:31
 * @Description:
 */
@RestController
@RequestMapping("/userMode/common/")
public class CommonController {

    @Resource
    private CommonService commonService;


    /**
     *
     * 功能描述: 校验手机验证码
     *
     * @param: [request, commonVo]
     * @return: void
     * @auther: tangpd
     * @date: 2020/12/30 18:13
     */
    @PostMapping("checkSMSCode")
    public ResultJsonBean checkSMSCode(HttpServletRequest request, @RequestBody CommonVo commonVo) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(commonVo, new String[]{"phoneNum", "smsCode"});
        return commonService.checkSMSCode(request, commonVo);
    }


    /**
     *
     * 功能描述: 获取手机验证码，需要先验证图片验证码
     *
     * @param: [request, commonVo]
     * @return: void
     * @auther: tangpd
     * @date: 2020/12/30 11:48
     */
    @PostMapping("getSMSCode")
    public ResultJsonBean getSMSCode(HttpServletRequest request, @RequestBody CommonVo commonVo) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(commonVo, new String[]{"phoneNum", "imgCode"});
        return commonService.getSMSCode(request, commonVo);
    }


    /**
     *
     * 功能描述: 获取图片验证码
     *
     * @param: [request, response]
     * @return: void
     * @auther: tangpd
     * @date: 2020/12/29 17:59
     */
    @GetMapping("getImgCode")
    public void getImgCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        commonService.getImgCode(request, response);
    }

}
