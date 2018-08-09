package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service注解：服务
 *
 * @author zhangguoli.cn
 * @date 2018/08/10
 */

//作用于接口、类
@Target(ElementType.TYPE)
//保留到运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    String value() default "";
}
