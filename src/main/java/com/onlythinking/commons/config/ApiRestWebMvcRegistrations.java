package com.onlythinking.commons.config;

import com.onlythinking.commons.config.annotation.ApiRest;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * <p> The describe </p>
 *
 * @author Li Xingping
 */
public class ApiRestWebMvcRegistrations extends WebMvcRegistrationsAdapter {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping() {
            private final static String API_BASE_PATH = "api";

            @Override
            protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
                Class<?> beanType = method.getDeclaringClass();
                ApiRest apiRest = AnnotationUtils.findAnnotation(beanType, ApiRest.class);
                if (null != apiRest) {
                    PatternsRequestCondition apiPattern = new PatternsRequestCondition(API_BASE_PATH + "/" + apiRest.serviceId())
                      .combine(mapping.getPatternsCondition());

                    mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
                      mapping.getMethodsCondition(), mapping.getParamsCondition(),
                      mapping.getHeadersCondition(), mapping.getConsumesCondition(),
                      mapping.getProducesCondition(), mapping.getCustomCondition());
                }

                super.registerHandlerMethod(handler, method, mapping);
            }

            @Override
            protected boolean isHandler(Class<?> beanType) {
                return super.isHandler(beanType) && null == AnnotationUtils.findAnnotation(beanType, FeignClient.class);
            }
        };
    }
}
