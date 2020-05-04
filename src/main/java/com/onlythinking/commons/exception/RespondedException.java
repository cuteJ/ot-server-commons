package com.onlythinking.commons.exception;

import lombok.Getter;

/**
 * <p> API 响应异常 </p>
 *
 * @author Li Xingping
 */
@Getter
public class RespondedException extends RuntimeException {

    private final int errorCode;
    private String errorMsg;
    private Object[] msgArgs;

    public RespondedException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = message;
    }

    public RespondedException(int errorCode, String message, Object... msgArgs) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = message;
        this.msgArgs = msgArgs;
    }

    public RespondedException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMsg = message;
    }

    public RespondedException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public RespondedException(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * <p>构造响应异常</p>
     *
     * @param errorCode 错误码
     * @param errorMsg  Message| Message Code
     * @param msgArgs   Message args
     * @return RespondedException
     */
    public static RespondedException of(int errorCode, String errorMsg, Object... msgArgs) {
        return new RespondedException(errorCode, errorMsg, msgArgs);
    }

    /**
     * <p>实体或方法参数约束校验不通过</p>
     *
     * @param errorMsg Message| Message Code
     * @param msgArgs  Message args
     * @return RespondedException
     */
    public static RespondedException violationError(String errorMsg, Object... msgArgs) {
        throw of(GlobalErrorCode.API_VIOLATION_ERROR, errorMsg, msgArgs);
    }

    /**
     * <p>API请求参数校验不通过</p>
     *
     * @param errorMsg Message| Message Code
     * @param msgArgs  Message args
     * @return RespondedException
     */
    public static RespondedException argumentInvalid(String errorMsg, Object... msgArgs) {
        throw of(GlobalErrorCode.API_ARGUMENT_INVALID, errorMsg, msgArgs);
    }

}
