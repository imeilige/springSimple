package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RequestMapping注解：请求路径
 *
 * @author zhangguoli.cn
 * @date 2018/08/10
 */

//作用于方法
@Target(ElementType.METHOD)
//保留到运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value() default "";
}
