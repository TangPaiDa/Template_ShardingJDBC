package cn.tang.userMode.service;

import cn.tang.common.utils.ResultJsonBean;
import cn.tang.userMode.vo.CommonVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: tangpd
 * @Date: 2020/12/29 16:34
 * @Description:
 */
public interface CommonService {

    ResultJsonBean checkSMSCode(HttpServletRequest request, CommonVo commonVo);

    ResultJsonBean getSMSCode(HttpServletRequest request, CommonVo commonVo);

    void getImgCode(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
