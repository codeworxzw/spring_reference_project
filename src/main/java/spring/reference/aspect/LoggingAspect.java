package spring.reference.aspect;

import javax.inject.Named;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spring.reference.util.Logged;

@Named
@Aspect
public class LoggingAspect {
    @Around("@within(loggedAnnotation) || @annotation(loggedAnnotation)")
    public Object log(ProceedingJoinPoint proceedingJoinPoint, Logged loggedAnnotation) throws Throwable {
        String className = proceedingJoinPoint.getTarget().getClass().getName();

        String methodName = proceedingJoinPoint.getSignature().getName();
        if (className.indexOf("$") > 0) {
            className = className.substring(0, className.indexOf("$"));
        }

        Logger logger = LoggerFactory.getLogger(className);

        if (isBooleanMethod(proceedingJoinPoint)) {
            return logBooleanMethod(proceedingJoinPoint, methodName, logger);
        } else {
            return logNormalMethod(proceedingJoinPoint, methodName, logger);
        }
    }

    private Object logNormalMethod(ProceedingJoinPoint proceedingJoinPoint, String methodName, Logger logger) throws Throwable {
        long startTime = System.currentTimeMillis();

        logger.debug(methodName + " STARTED");

        logMethodParameters(proceedingJoinPoint, methodName, logger);

        try {
            Object retValue = proceedingJoinPoint.proceed();
            logger.debug(methodName + " FINISHED in " + (System.currentTimeMillis() - startTime) + " ms: " + retValue);
            return retValue;
        } catch (Throwable e) {
            logger.warn(methodName + " FAILED in " + (System.currentTimeMillis() - startTime) + " ms");
            throw e;
        }
    }

    private Object logBooleanMethod(ProceedingJoinPoint proceedingJoinPoint, String methodName, Logger logger) throws Throwable {
        try {
            Object retValue = proceedingJoinPoint.proceed();

            logger.debug(methodName + " " + retValue.toString().toUpperCase());
            logMethodParameters(proceedingJoinPoint, methodName, logger);

            return retValue;
        } catch (Throwable e) {
            logger.debug(methodName + "FAILED");
            throw e;
        }
    }

    private void logMethodParameters(ProceedingJoinPoint proceedingJoinPoint, String methodName, Logger logger) {
        Object[] objs = proceedingJoinPoint.getArgs();
        if (objs != null) {
            int i = 1;
            for (Object o : objs) {
                String message = i++ + ": " + methodName + " " + (o != null ? o.toString() : "null");
                logger.debug(message);
            }
        }
    }

    private boolean isBooleanMethod(ProceedingJoinPoint proceedingJoinPoint) {
        Class<?> returnType = ((MethodSignature) proceedingJoinPoint.getSignature()).getReturnType();
        return Boolean.class.equals(returnType) || boolean.class.equals(returnType);
    }
}
