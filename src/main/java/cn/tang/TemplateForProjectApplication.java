package cn.tang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("cn.tang.*.dao")//将项目中对应的mapper类的路径加进来
public class TemplateForProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplateForProjectApplication.class, args);
	}

}

