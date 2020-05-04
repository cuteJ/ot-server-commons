package com.onlythinking.commons.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * <p> The describe </p>
 *
 * @author Li Xingping
 */
@Component
public class MessagesHelper {

    private final MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @Autowired
    public MessagesHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.SIMPLIFIED_CHINESE);
    }

    public String get(String code) {
        return accessor.getMessage(code);
    }

    public String get(String code, Locale locale) {
        return accessor.getMessage(code, locale);
    }

    public String get(String code, Object[] args) {
        if (null == args) {
            return get(code);
        }
        return accessor.getMessage(code, args);
    }

}
