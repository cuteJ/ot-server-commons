package com.onlythinking.commons.core.track;

import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.onlythinking.commons.exception.GlobalErrorCode;
import com.onlythinking.commons.exception.RespondedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p> The describe </p>
 *
 * @author Li Xingping
 */
@Slf4j
@Aspect
public class SimpleProcessTracking {

    private final static String ERROR_LOG_PREFIX = "TRACK_ERROR ";

    @Pointcut("within(com.onlythinking..*)")
    public void onlythinking() {
    }

    @Around("onlythinking() && @annotation(tracking)")
    public Object tracking(ProceedingJoinPoint pjp, Tracking tracking) throws Throwable {
        Object[] args = pjp.getArgs();
        Signature signature = pjp.getSignature();
        String targetMethod = signature.getDeclaringTypeName() + "." + signature.getName();
        log.info(ERROR_LOG_PREFIX + "-----------------------------------{}--------------------------->>>>>", tracking.value());
        Optional.of(args).ifPresent(a -> log.info(ERROR_LOG_PREFIX + "请求参数：{}", Arrays.toString(a)));
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            Object retVal = pjp.proceed();
            stopwatch.stop();
            log.info(ERROR_LOG_PREFIX + "Method call time: {}", stopwatch);
            log.info(ERROR_LOG_PREFIX + "{} return: {}", targetMethod, retVal);
            if (-1 != tracking.timeout()) {
                long elapsedMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                if (elapsedMillis > tracking.timeout()) {
                    throw new RespondedException(GlobalErrorCode.API_REQUEST_TIMEOUT, String.format("%s call exceeds expected %s millis", targetMethod, tracking.timeout()));
                }
            }
            return retVal;
        } catch (Exception e) {
            log.error(ERROR_LOG_PREFIX + "error: {}", Throwables.getRootCause(e).getMessage());
            throw e;
        }
    }
}
