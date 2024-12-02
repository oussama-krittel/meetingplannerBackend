package com.zenika.meetingplanner.common.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ApiLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingAspect.class);

    /**
     * Logs information about controller methods.
     *
     * @param joinPoint The join point providing method details.
     * @return The result of the method execution.
     * @throws Throwable If the method execution fails.
     */
    @Around("execution(* com.zenika.meetingplanner.adapters.inbound.rest..*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // Log method entry
        String methodName = joinPoint.getSignature().toShortString();
        Object[] methodArgs = joinPoint.getArgs();
        logger.info("Entering method: {} with arguments: {}", methodName, Arrays.toString(methodArgs));

        Object result;
        try {
            // Execute the method
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in method: {} with arguments: {}", methodName, Arrays.toString(methodArgs), throwable);
            throw throwable;
        }

        // Log method exit
        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("Exiting method: {} with result: {} (Execution time: {} ms)", methodName, result, elapsedTime);

        return result;
    }
}
