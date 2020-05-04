package com.onlythinking.commons.exception;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p> The describe </p>
 *
 * @author Li Xingping
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestRespondedExceptionHandler extends AbstractRespondedExceptionHandler {

    public RestRespondedExceptionHandler(ErrorProperties errorProperties, ErrorAttributes errorAttributes, MessageSource messageSource) {
        super(errorProperties, errorAttributes, messageSource);
    }
}
