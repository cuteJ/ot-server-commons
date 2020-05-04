package com.onlythinking.commons.config;

import com.onlythinking.commons.core.track.SimpleProcessTracking;
import com.onlythinking.commons.exception.RestErrorController;
import com.onlythinking.commons.exception.RestRespondedExceptionHandler;
import com.onlythinking.commons.helper.MessagesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> The describe </p>
 *
 * @author Li Xingping
 */
@Configuration
public class CommonsModuleConfiguration {

    private final MessageSource messageSource;

    @Autowired
    public CommonsModuleConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestRespondedExceptionHandler respondedExceptionHandler(ServerProperties serverProperties,
                                                                   ErrorAttributes errorAttributes) {
        return new RestRespondedExceptionHandler(serverProperties.getError(), errorAttributes, messageSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestErrorController restErrorController(ServerProperties serverProperties,
                                                   ErrorAttributes errorAttributes) {
        return new RestErrorController(serverProperties.getError(), errorAttributes, messageSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public MessagesHelper messagesHelper() {
        return new MessagesHelper(messageSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleProcessTracking simpleProcessTracking() {
        return new SimpleProcessTracking();
    }

}
