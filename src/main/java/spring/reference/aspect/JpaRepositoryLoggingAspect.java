package spring.reference.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class JpaRepositoryLoggingAspect {
    private LoggingAspect loggingAspect = new LoggingAspect();

    // Don't forget to configure logging accordingly!
    @Around("execution(* org.springframework.data.jpa.repository.support.SimpleJpaRepository.*(..))")
    public Object logJpaRepository(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return loggingAspect.log(proceedingJoinPoint, null);
    }
}
