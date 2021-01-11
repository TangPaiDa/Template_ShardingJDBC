package cn.tang.common.jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: tangpd
 * @Date: 2020/12/11 13:51
 * @Description:
 */
@Configuration
public class JwtInterceptorConfiguration implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**")//默认全部拦截
                .excludePathPatterns("/userMode/sysUser/login")//用户登录放行
                .excludePathPatterns("/userMode/sysUser/getCheckImg");//获取图片验证码放行

    }

}
