package com.onlythinking.commons.core.interceptor;

import java.lang.annotation.*;

/**
 * <p> The describe </p>
 *
 * @author Li Xingping
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DisabledField {
    String value() default "disabled";

    boolean val() default false;
}
