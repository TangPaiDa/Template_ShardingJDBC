package cn.tang.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @Auther: tangpd
 * @Date: 2019/1/9 16:58
 * @Description: 演示AOP
 */
@Aspect
@Component//将文件引入到spring容器里
@Slf4j
public class RequestInformationAspect {

    @Pointcut("execution(public * cn.tang.*.controller.*.*(..))")
    public void log() {   }

    @Before("log()")
    public void  doBefore(JoinPoint joinPoint){
        //获取request对象
        ServletRequestAttributes aattributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = aattributes.getRequest();
        log.info("");
        log.info("");
        log.info("have a new request ->(****************************************************************)");
        //客户端请求的url
        log.info("the request url is ->( {} ):{}{}{}",request.getRequestURI(), " from '", request.getRemoteAddr(), "' request;");
        //处理接口的类方法
        log.info("the handle method is ->( {}(..) ):{}[{}.java];",joinPoint.getSignature().getName(), "the method is inside ", joinPoint.getSignature().getDeclaringTypeName() );
        //参数
        Map<String, String[]> map = request.getParameterMap();
        Iterator<Map.Entry<String, String[]>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String[]> entry = entries.next();
            for (int i = 0; i < entry.getValue().length; i++) {
                log.info("the parameter is ->( " + entry.getKey() + " ):{}",entry.getValue()[i]);
            }
        }
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public  void  doAfterReturning(Object object){
        //方法正常执行之后返回的结果
        log.info("the request is success ->( and response is ): {}",object);
        log.info("the request is end ->(================================================================)");
    }

}
