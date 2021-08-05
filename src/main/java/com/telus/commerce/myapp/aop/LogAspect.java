package com.telus.commerce.myapp.aop;

import static net.logstash.logback.marker.Markers.append;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

@Aspect
@Configuration
public class LogAspect {

  private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

  @Pointcut("execution(* com.telus.commerce.myapp.service..*(..))")
  protected void ServiceLayer() {}

  @Pointcut("execution(* com.telus.commerce.myapp.controller..*(..))")
  protected void controller() {}

  @Around("ServiceLayer() ||  controller()")
  public Object cacheGetOperaetions(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Object retVal = joinPoint.proceed();
    stopWatch.stop();
    logger.info(
        ((append("operation", joinPoint.getSignature().getDeclaringTypeName()))
            .and(append("executionTime", stopWatch.getTotalTimeMillis()))),
        "Exit: {}.{}() using {} ms, with result = {}",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(),
        stopWatch.getTotalTimeMillis(),
        retVal);
    return retVal;
  }
}
