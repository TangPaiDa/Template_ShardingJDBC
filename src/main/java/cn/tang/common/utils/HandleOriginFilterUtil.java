package cn.tang.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @Auther: tangpd
 * @Date: 2020/12/8 14:52
 * @Description:
 */
@WebFilter(urlPatterns = "/*", filterName = "handleOriginFilterUtil")
@Order(1)
@Slf4j
public class HandleOriginFilterUtil implements Filter{

            public void destroy() {}

            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                handleOrigin(servletRequest, servletResponse);//处理跨域
                //doFilterMethod(servletRequest);//打印接口参数
                filterChain.doFilter(servletRequest, servletResponse);
            }

            public void init(FilterConfig arg0) {}

            //===================================================================================================================================================
            /**
             *
             * 功能描述: 跨域问题处理
             *
             * @param: [servletRequest, servletResponse]
             * @return: void
             * @auther: tangpd
             * @date: 2020/12/8 15:01
             */
            private void handleOrigin(ServletRequest servletRequest, ServletResponse servletResponse) {

                HttpServletRequest request = (HttpServletRequest) servletRequest;
                HttpServletResponse response = (HttpServletResponse) servletResponse;

                //1、增加域名，允许跨域
                String Origin = request.getHeader("Origin");
                if(Origin != "" && Origin != null) {
                    response.setHeader("Access-Control-Allow-Origin", Origin);
                } else {
                    response.setHeader("Access-Control-Allow-Origin", "*");
                }

                //2、表示允许所有请求，包括POST, GET, OPTIONS, DELETE；
                response.setHeader("Access-Control-Allow-Methods", "*");

                //3、处理非简单请求，OPTIONS预检命令时用到，支持所有自定义头
                String headers = request.getHeader("Access-Control-Request-Headers");
                if(headers != "" && headers != null) {
                    response.setHeader("Access-Control-Allow-Headers", headers);
                }

                //4、处理非简单请求，OPTIONS预检命令多次发送会影响性能，可以使用缓存，告诉浏览器再1小时之内可以不用再次检查非简单请求
                response.setHeader("Access-Control-Max-Age", "3600");

                //5、携带cooki的时候，响应头要有这个值，要是true；
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }

            /**
             *
             * 功能描述: 此方法可以打印接口url、参数，但是不全，看你喜欢
             *
             * @param: [servletRequest]
             * @return: void
             * @auther: tangpd
             * @date: 2020/12/8 14:59
             */
            private void doFilterMethod(ServletRequest servletRequest) {
                HttpServletRequest request = (HttpServletRequest) servletRequest;
                log.error("URL----------" + request.getRequestURL());
                Enumeration<String> names = request.getParameterNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    String value = request.getParameter(name);
                    log.info("param----------" + name + " ： " + value);
                }
            }


}
