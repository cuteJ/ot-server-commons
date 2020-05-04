package com.onlythinking.commons.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <p> API 响应 CODE 对照表 </p>
 *
 * @author Li Xingping
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalErrorCode {

    //-----------------------------------------常用错误状态(0x7530-0x9c3F)---------------------------------------------------

    //-----------------------------------------业务错误状态(0x9C40-0xC349)---------------------------------------------------

    //-----------------------------------------Http错误状态(0x0100-0x01FF)-----------------------------------------------------
    public static final int SC_BAD_REQUEST = 400,
      SC_UNAUTHORIZED = 401,
      SC_PAYMENT_REQUIRED = 402,
      SC_FORBIDDEN = 403,
      SC_NOT_FOUND = 404,
      SC_METHOD_NOT_ALLOWED = 405,
      SC_NOT_ACCEPTABLE = 406,
      SC_PROXY_AUTHENTICATION_REQUIRED = 407,
      SC_REQUEST_TIMEOUT = 408,
      SC_CONFLICT = 409,
      SC_GONE = 410,
      SC_LENGTH_REQUIRED = 411,
      SC_PRECONDITION_FAILED = 412,
      SC_PAYLOAD_TOO_LARGE = 413,
      SC_URI_TOO_LONG = 413,
      SC_UNSUPPORTED_MEDIA_TYPE = 415,
      SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416,
      SC_EXPECTATION_FAILED = 417,
      SC_I_AM_A_TEAPOT = 418,
      SC_UNPROCESSABLE_ENTITY = 422,
      SC_LOCKED = 423,
      SC_FAILED_DEPENDENCY = 424,
      SC_UPGRADE_REQUIRED = 426,
      SC_PRECONDITION_REQUIRED = 428,
      SC_TOO_MANY_REQUESTS = 429,
      SC_REQUEST_HEADER_FIELDS_TOO_LARGE = 431,
      SC_UNAVAILABLE_FOR_LEGAL_REASONS = 451,
      SC_INTERNAL_SERVER_ERROR = 500,
      SC_NOT_IMPLEMENTED = 501,
      SC_BAD_GATEWAY = 502,
      SC_SERVICE_UNAVAILABLE = 503,
      SC_GATEWAY_TIMEOUT = 504,
      SC_HTTP_VERSION_NOT_SUPPORTED = 505,
      SC_VARIANT_ALSO_NEGOTIATES = 506,
      SC_INSUFFICIENT_STORAGE = 507,
      SC_LOOP_DETECTED = 508,
      SC_BANDWIDTH_LIMIT_EXCEEDED = 509,
      SC_NOT_EXTENDED = 510,
      SC_NETWORK_AUTHENTICATION_REQUIRED = 511;

    //-----------------------------------------全局错误状态---------------------------------------------------
    public static final int SUCCESSFUL = 0x0000,    // 请求成功
      API_ARGUMENT_INVALID = 0x7531,    // API请求参数校验不通过（如: NotBlank NotEmpty）
      API_VIOLATION_ERROR = 0x7532,     // 实体或方法参数约束校验不通过
      API_REQUEST_TIMEOUT = 0x7533,     // API请求超时
      SYSTEM_BUSY = -1,   // 系统繁忙，请稍候再试
      UNDEFINED = 0xFFFF;
}
