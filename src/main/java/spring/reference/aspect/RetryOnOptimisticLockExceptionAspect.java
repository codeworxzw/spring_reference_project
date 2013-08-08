package spring.reference.aspect;

import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.transaction.RollbackException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.UnexpectedRollbackException;

import spring.reference.exception.OptimisticLockRetryException;
import spring.reference.service.RetryOnOptimisticLockException;

@Named
@Aspect
public class RetryOnOptimisticLockExceptionAspect {
    @Around("@within(retryOnOptimisticLockExceptionAnnotation) || @annotation(retryOnOptimisticLockExceptionAnnotation)")
    public Object retryOnOptimisticLockAspect(ProceedingJoinPoint proceedingJoinPoint, RetryOnOptimisticLockException retryOnOptimisticLockExceptionAnnotation)
            throws Throwable {
        String className = proceedingJoinPoint.getThis().getClass().getName();

        long retryDurationInMilliseconds = retryOnOptimisticLockExceptionAnnotation.retryDurationInMilliseconds();

        int junkStartIndex = className.indexOf("$");
        if (junkStartIndex > 0) {
            className = className.substring(0, junkStartIndex);
        }

        String methodName = proceedingJoinPoint.getSignature().getName();

        Logger logger = LoggerFactory.getLogger(className);

        Object outcome = null;

        long startTime = System.currentTimeMillis();
        boolean succesful = false;
        while (!succesful && System.currentTimeMillis() - startTime < retryDurationInMilliseconds * 1000) {
            try {
                outcome = proceedingJoinPoint.proceed();

                succesful = true;
            } catch (Exception e) {
                try {
                    processException(e);
                } catch (OptimisticLockException e1) {
                    logger.warn("Concurrent modification during " + methodName + ", retrying...");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e2) {
                        logger.warn("Thread interrupted while sleeping before retrying " + methodName + ".", e2);
                    }
                }
            }
        }

        if (!succesful) {
            String errorMessage = methodName + " failed.";
            logger.warn(errorMessage);
            throw new OptimisticLockRetryException(errorMessage);
        }

        return outcome;
    }

    private void processException(Exception e) throws Throwable {
        if (e != null && UnexpectedRollbackException.class.equals(e.getClass())) {
            Throwable nested = e.getCause();
            if (nested != null && RollbackException.class.equals(nested.getClass())) {
                nested = nested.getCause();
                if (nested != null && OptimisticLockException.class.equals(nested.getClass())) {
                    throw nested;
                }
            }
        }
        throw e;
    }
}
