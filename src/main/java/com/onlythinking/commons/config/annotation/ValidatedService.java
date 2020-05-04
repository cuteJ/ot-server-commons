package com.onlythinking.commons.config.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.*;

/**
 * <p> The validated service . </p>
 *
 * @author Li Xingping
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Validated
@Service
public @interface ValidatedService {

    @AliasFor(annotation = Validated.class, attribute = "value")
    Class<?>[] validatedValue() default {};

    @AliasFor(annotation = Service.class)
    String value() default "";
}
