package com.onlythinking.commons.exception;

import com.google.common.base.Throwables;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Set;

/**
 * <p> API 全局异常处理类 </p>
 *
 * @author Li Xingping
 */
@Slf4j
@Getter
public abstract class AbstractRespondedExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorProperties errorProperties;
    private final ErrorAttributes errorAttributes;
    private final MessageSource messageSource;

    protected AbstractRespondedExceptionHandler(ErrorProperties errorProperties, ErrorAttributes errorAttributes, MessageSource messageSource) {
        this.errorProperties = errorProperties;
        this.errorAttributes = errorAttributes;
        this.messageSource = messageSource;
    }

    /**
     * 已定义API响应异常
     *
     * @param ex 自定义异常
     * @return ResponseEntity
     */
    @ExceptionHandler({RespondedException.class})
    @ResponseBody
    public ResponseEntity<Object> handleRespondedException(final RespondedException ex) {
        String errorMsg = ex.getErrorMsg();
        String errorCode = StringUtils.substringBetween(ex.getErrorMsg(), "{", "}");
        if (StringUtils.isNotBlank(errorCode)) {
            errorMsg = messageSource.getMessage(errorCode, ex.getMsgArgs(), LocaleContextHolder.getLocale());
        }
        if (null != ex.getCause()) {
            log.error(ex.getMessage());
        } else {
            log.warn("errorCode:{} message:{}", ex.getErrorCode(), errorMsg);
        }
        return ResponseEntity.ok(RespondedBody.of(ex.getErrorCode(), errorMsg));
    }

    @ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
    @ResponseBody
    public ResponseEntity<Object> handleUnsatisfiedException(final UnsatisfiedServletRequestParameterException ex) {
        log.error(ex.getMessage());
        String errorMsg = String.format("%s 不能为空", Arrays.toString(ex.getParamConditions()));
        return ResponseEntity.ok(RespondedBody.of(GlobalErrorCode.API_ARGUMENT_INVALID, errorMsg));
    }

    /**
     * Controller 参数校验不通过
     *
     * @param ex      异常
     * @param headers Header
     * @param request 请求
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.ok(onMethodArgumentNotValidException(ex));
    }

    /**
     * 约束不通过
     *
     * @param ex 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.ok(onConstraintViolationException(ex));
    }

    /**
     * 事务异常
     *
     * @param ex      异常
     * @param request 请求
     * @return ResponseEntity
     */
    @ExceptionHandler({TransactionSystemException.class})
    @ResponseBody
    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex, WebRequest request) {
        Throwable cause = Throwables.getRootCause(ex);
        errorLog(ex);
        if (cause instanceof ConstraintViolationException) {
            return ResponseEntity.ok(onConstraintViolationException((ConstraintViolationException) cause));
        }
        return handleExceptionInternal(ex, request);
    }

    /**
     * 并发冲突异常
     *
     * @param ex      异常
     * @param request Web request
     * @return ResponseEntity
     */
    @ExceptionHandler({InvalidDataAccessApiUsageException.class, DataAccessException.class, DataIntegrityViolationException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, WebRequest request) {
        errorLog(ex);
        return handleExceptionInternal(ex, request);
    }

    /**
     * 运行异常
     *
     * @param ex      异常
     * @param request Web request
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, WebRequest request) {
        errorLog(ex);
        return handleExceptionInternal(ex, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 内部异常处理
     *
     * @param ex 异常
     * @return ResponseEntity
     */
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        errorLog(ex);
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        if (ErrorRespondedHelper.isIncludeStackTrace(this.errorProperties.getIncludeStacktrace())) {
            return ResponseEntity.ok(ErrorRespondedHelper.handleResult(status, Throwables.getRootCause(ex).getMessage()));
        }
        int errorCode = ErrorRespondedHelper.handleResultFriendly(status);
        return ResponseEntity.ok(RespondedBody.of(errorCode, messageSource.getMessage("http.error." + errorCode, null, LocaleContextHolder.getLocale())));
    }

    private void errorLog(Exception exStack) {
        log.error(Throwables.getStackTraceAsString(exStack));
    }

    private RespondedBody onConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation constraintViolation : constraintViolations) {
            errorMessage.append(constraintViolation.getMessage());
        }
        return RespondedBody.of(GlobalErrorCode.API_VIOLATION_ERROR, errorMessage.toString());
    }

    private RespondedBody onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                errorMessage.append(((FieldError) error).getField());
            }
            errorMessage.append(error.getDefaultMessage()).append(StringUtils.SPACE);
        }
        return RespondedBody.of(GlobalErrorCode.API_ARGUMENT_INVALID, errorMessage.toString());
    }
}
