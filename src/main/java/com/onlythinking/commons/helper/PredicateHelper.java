package com.onlythinking.commons.helper;

import com.onlythinking.commons.exception.GlobalErrorCode;
import com.onlythinking.commons.exception.RespondedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * <p> 校验工具类 </p>
 *
 * @author Li Xingping
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PredicateHelper {

    /**
     * <p>前置不能为空</p>
     *
     * @param <T>          实体
     * @param reference    实体
     * @param errorMessage Message| Message Code
     * @return t
     */
    public static <T> T checkNotNull(T reference, String errorMessage) {
        if (null == reference) {
            throw new RespondedException(GlobalErrorCode.API_VIOLATION_ERROR, errorMessage);
        }
        return reference;
    }

    /**
     * <p>前置不能为空</p>
     *
     * @param <T>          集合
     * @param reference    集合
     * @param errorMessage Message| Message Code
     * @return t
     */
    public static <T extends Collection> T checkNotNull(T reference, String errorMessage) {
        if (CollectionUtils.isEmpty(reference)) {
            throw new RespondedException(GlobalErrorCode.API_VIOLATION_ERROR, errorMessage);
        }
        return reference;
    }

    /**
     * <p>前置不能为空</p>
     *
     * @param reference    字符串
     * @param errorMessage Message| Message Code
     * @return t
     */
    public static String checkNotNull(String reference, String errorMessage) {
        if (StringUtils.isBlank(reference)) {
            throw new RespondedException(GlobalErrorCode.API_VIOLATION_ERROR, errorMessage);
        }
        return reference;
    }

    /**
     * <p>前置条件必须为真</p>
     *
     * @param expression   实体
     * @param errorMessage Message| Message Code
     */
    public static void checkArg(boolean expression, String errorMessage) {
        if (!expression) {
            throw new RespondedException(GlobalErrorCode.API_VIOLATION_ERROR, errorMessage);
        }
    }

    /**
     * <p>前置不能为空</p>
     *
     * @param <T>          实体
     * @param reference    实体
     * @param errorMessage Message| Message Code
     * @param consumer     非空回调
     */
    public static <T> void ifNotNull(T reference, String errorMessage, Consumer<T> consumer) {
        if (null == reference) {
            throw new RespondedException(GlobalErrorCode.API_VIOLATION_ERROR, errorMessage);
        }
        consumer.accept(reference);
    }

}
