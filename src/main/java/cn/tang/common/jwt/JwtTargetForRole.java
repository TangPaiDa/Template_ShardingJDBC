package cn.tang.common.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: tangpd
 * @Date: 2020/12/11 11:40
 * @Description: 基于角色控制的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtTargetForRole {
    String value() default "";
}
