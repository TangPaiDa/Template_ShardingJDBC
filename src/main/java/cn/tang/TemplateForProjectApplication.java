package cn.tang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("cn.tang.*.dao")//将项目中对应的mapper类的路径加进来
@ServletComponentScan//开启过滤器功能
//@EnableScheduling //开启定时任务，打开注解之后启动项目即可自动执行；可在cn.tang.commo.task.TaskForTimingUtil里面设置定时任务
//@EnableAsync //开启异步执行任务，打开注解之后，还需要触发才可执行，优势在于 cn.tang.commo.task.TaskForAsyncUtil 里面的方法被调用之后可以异步执行；
public class TemplateForProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplateForProjectApplication.class, args);
	}

}

