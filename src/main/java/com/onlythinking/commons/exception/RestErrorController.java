package com.onlythinking.commons.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

import static com.onlythinking.commons.exception.ErrorRespondedHelper.*;

/**
 * <p> The describe </p>
 *
 * @author Li Xingping
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class RestErrorController implements ErrorController {

    private final ErrorProperties errorProperties;
    private final ErrorAttributes errorAttributes;
    private final MessageSource messageSource;

    @RequestMapping
    @ResponseBody
    public RespondedBody error(HttpServletRequest request, Locale locale) {
        boolean includeStackTrace = isIncludeStackTrace(request, errorProperties.getIncludeStacktrace());
        Map<String, Object> body = getErrorAttributes(request, errorAttributes, errorProperties.getIncludeStacktrace());
        String errorMsg = (String) body.get("message");
        int errorCode = ErrorRespondedHelper.handleResultFriendly(getStatus(request));
        if (includeStackTrace) {
            errorMsg = body.toString();
        } else if (ErrorProperties.IncludeStacktrace.NEVER == errorProperties.getIncludeStacktrace()) {
            errorMsg = messageSource.getMessage("http.error." + errorCode, null, locale);
        }
        return RespondedBody.of(errorCode, errorMsg);
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }
}
