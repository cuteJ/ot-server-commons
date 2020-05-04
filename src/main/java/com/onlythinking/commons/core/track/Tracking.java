package com.onlythinking.commons.core.track;

import java.lang.annotation.*;

/**
 * <p> 日志追踪 </p>
 *
 * @author Li Xingping
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Tracking {
    /**
     * @return 标记
     */
    String value() default "";

    /**
     * @return 方法超时时间 单位毫秒
     */
    long timeout() default -1;
}
