package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller注解：控制器
 *
 * @author zhangguoli.cn
 * @date 2018/08/10
 */

//作用于接口、类
@Target(ElementType.TYPE)
//保留到运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String value() default "";
}
