package com.onlythinking.commons.exception;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

import static com.onlythinking.commons.exception.GlobalErrorCode.*;

/**
 * <p> The describe </p>
 *
 * @author Li Xingping
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorRespondedHelper {

    public static boolean isIncludeStackTrace(HttpServletRequest request,
                                              ErrorProperties.IncludeStacktrace include) {
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    public static boolean isIncludeStackTrace(ErrorProperties.IncludeStacktrace include) {
        return include == ErrorProperties.IncludeStacktrace.ALWAYS;
    }


    public static boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    public static HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public static Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                         ErrorAttributes errorAttributes,
                                                         ErrorProperties.IncludeStacktrace include) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, isIncludeStackTrace(request, include));
    }


    private final static Set<Integer> HTTP_STATUS = Sets.newHashSet(
      SC_BAD_REQUEST,
      SC_UNAUTHORIZED,
      SC_PAYMENT_REQUIRED,
      SC_FORBIDDEN,
      SC_NOT_FOUND,
      SC_METHOD_NOT_ALLOWED,
      SC_NOT_ACCEPTABLE,
      SC_PROXY_AUTHENTICATION_REQUIRED,
      SC_REQUEST_TIMEOUT,
      SC_CONFLICT,
      SC_GONE,
      SC_LENGTH_REQUIRED,
      SC_PRECONDITION_FAILED,
      SC_PAYLOAD_TOO_LARGE,
      SC_URI_TOO_LONG,
      SC_UNSUPPORTED_MEDIA_TYPE,
      SC_REQUESTED_RANGE_NOT_SATISFIABLE,
      SC_EXPECTATION_FAILED,
      SC_I_AM_A_TEAPOT,
      SC_UNPROCESSABLE_ENTITY,
      SC_LOCKED,
      SC_FAILED_DEPENDENCY,
      SC_UPGRADE_REQUIRED,
      SC_PRECONDITION_REQUIRED,
      SC_TOO_MANY_REQUESTS,
      SC_REQUEST_HEADER_FIELDS_TOO_LARGE,
      SC_UNAVAILABLE_FOR_LEGAL_REASONS,
      SC_INTERNAL_SERVER_ERROR,
      SC_NOT_IMPLEMENTED,
      SC_BAD_GATEWAY,
      SC_SERVICE_UNAVAILABLE,
      SC_GATEWAY_TIMEOUT,
      SC_HTTP_VERSION_NOT_SUPPORTED,
      SC_VARIANT_ALSO_NEGOTIATES,
      SC_INSUFFICIENT_STORAGE,
      SC_LOOP_DETECTED,
      SC_BANDWIDTH_LIMIT_EXCEEDED,
      SC_NOT_EXTENDED,
      SC_NETWORK_AUTHENTICATION_REQUIRED
    );

    public static RespondedBody handleResult(HttpStatus status, String errorMsg) {
        return RespondedBody.of(handleResultFriendly(status), errorMsg);
    }

    public static int handleResultFriendly(HttpStatus status) {
        return HTTP_STATUS.contains(status.value()) ? status.value() : UNDEFINED;
    }
}
