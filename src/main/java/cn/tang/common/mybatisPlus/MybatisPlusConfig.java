package cn.tang.common.mybatisPlus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {


	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {

		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		paginationInnerInterceptor.setOverflow(true);//溢出总页数后是否进行处理，默认false
		//paginationInnerInterceptor.setMaxLimit(500l);//单页分页条数限制, 默认无限制
		paginationInnerInterceptor.setDbType(DbType.MYSQL);//设置数据库类型

		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(paginationInnerInterceptor);
		return interceptor;
	}



}
