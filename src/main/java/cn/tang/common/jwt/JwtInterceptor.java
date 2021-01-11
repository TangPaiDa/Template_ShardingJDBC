package cn.tang.common.jwt;

import cn.tang.common.utils.ExceptionForMyCustom;
import cn.tang.common.utils.ResultJsonEunm;
import cn.tang.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static cn.tang.common.jwt.JwtUtil.getInfoByTokenAndName;

/**
 * @Auther: tangpd
 * @Date: 2020/12/11 11:34
 * @Description:
 */
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1、获取接口相关数据
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String methodName = method.getName();
        //log.info("拦截开始**************************（1）====拦截到了方法：{}，在该方法执行之前执行====", methodName);
        String jwtToken = request.getHeader("Authorization");
        //log.info("获取到jwtToken**************************（jwtToken）{}", jwtToken);
        if (StringUtil.is_empty(jwtToken)){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_NOT_LOGIN_ERROR);
        }
        if (!JwtUtil.validationToken(jwtToken)){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_TOKEN_ERROR);
        }

        //2、判断该接口是否有角色控制
        JwtTargetForRole jwtTargetForRole = method.getAnnotation(JwtTargetForRole.class);
        if (null != jwtTargetForRole) {
            //获取注解上设定的所需要的角色
            String methodRole = jwtTargetForRole.value();
            //获取用户所拥有的角色
            String userRole = getInfoByTokenAndName(jwtToken, "userRole");
            //若没有该角色则不放行
            if (StringUtil.is_empty(userRole) || userRole.indexOf(methodRole) < 0){
                throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_ROLE_ERROR, "缺少角色：" + methodRole);
            }
        }

        //3、判断该接口是否有权限控制
        JwtTargetForPermissions jwtTargetForPermissions = method.getAnnotation(JwtTargetForPermissions.class);
        if (null != jwtTargetForPermissions) {
            //获取注解上设定的所需要的角色
            String methodPermissions = jwtTargetForPermissions.value();
            //获取用户所拥有的角色
            String userPermissions = getInfoByTokenAndName(jwtToken, "userPermissions");
            //若没有该权限则不放行
            if (userPermissions.indexOf(methodPermissions) < 0){
                throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_USER_ROLE_ERROR, "缺少权限：" + methodPermissions);
            }
        }

        //4、需要拦截的资源，用户已登录，并且满足角色、权限则放行
        return true;
    }



}
