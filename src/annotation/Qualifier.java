package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Qualifier注解：指定bean
 *
 * @author zhangguoli.cn
 * @date 2018/08/10
 */

//作用于变量上
@Target(ElementType.FIELD)
//保留到运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {
    String value() default "";
}
