package com.onlythinking.commons.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p> 全局响应 </p>
 *
 * @author Li Xingping
 */
@ApiModel("全局响应")
@Getter
@ToString
public final class RespondedBody implements Serializable {

    @ApiModelProperty(value = "返回码", example = "0", required = true)
    @JsonProperty("errorCode")
    private final int errorCode;

    @ApiModelProperty(value = "说明", example = "请求成功", position = 1)
    @JsonProperty("message")
    private final String message;

    @JsonCreator
    public RespondedBody(@JsonProperty("errorCode") @NotNull int errorCode,
                         @JsonProperty("message") String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static RespondedBody of(int errorCode, String errorMsg) {
        return new RespondedBody(errorCode, errorMsg);
    }

    public static RespondedBody of(int errorCode) {
        return new RespondedBody(errorCode, StringUtils.EMPTY);
    }

    public static RespondedBody successful() {
        return new RespondedBody(GlobalErrorCode.SUCCESSFUL, "Ok");
    }

    public static RespondedBody successful(String message) {
        return new RespondedBody(GlobalErrorCode.SUCCESSFUL, message);
    }

    public static RespondedBody undefined() {
        return new RespondedBody(GlobalErrorCode.UNDEFINED, "Undefined error.");
    }


}
