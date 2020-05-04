package com.onlythinking.commons.config.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * <p> The validated controller. </p>
 *
 * @author Li Xingping
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
@Validated
public @interface ApiRest {

    /**
     * @return ServiceId spring.application.name
     */
    String serviceId();

    @AliasFor(annotation = RestController.class, attribute = "value")
    String controllerName() default "";


    @AliasFor(annotation = RequestMapping.class)
    RequestMethod[] method() default {};


    @AliasFor(annotation = RequestMapping.class)
    String name() default "";


    @AliasFor(annotation = RequestMapping.class)
    String[] value() default {};


    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};


    @AliasFor(annotation = RequestMapping.class)
    String[] params() default {};


    @AliasFor(annotation = RequestMapping.class)
    String[] headers() default {};


    @AliasFor(annotation = RequestMapping.class)
    String[] consumes() default {};


    @AliasFor(annotation = RequestMapping.class)
    String[] produces() default {MediaType.APPLICATION_JSON_UTF8_VALUE};

}
