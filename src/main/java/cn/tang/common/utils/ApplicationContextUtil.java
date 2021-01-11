package cn.tang.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangpd
 * @Date: 2020/10/23 11:14
 * @Description: 在某些没有注入 Spring 工程的类中，需要使用到 Spring 工程里面的对象时，
 * 比如用到 server、dao等，就可以通过这个工具类的方法获取
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    /**
     * 根据 bean 名称获取工厂中指定 bean 对象，例如:
     * UserServer userServer = (UserServer)ApplicationContextUtil.getBean("userServer");
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}
