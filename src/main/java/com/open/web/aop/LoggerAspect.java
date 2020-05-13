package com.open.web.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 *  controller层日志
 * @auther 程佳伟
 * @create 2019-10-13 01:18
 */
@Aspect
@Order(1)
@Component
@Log4j2
public class LoggerAspect {

    @Pointcut("execution(public * com.open.web.controller..*.*(..))")
    public void logForController(){}


    @Around(value = "logForController()")
    public Object aroundController(ProceedingJoinPoint joinPoint){

        Object result = null;
        HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
        String uri = request.getRequestURI();
        String requestMethod = request.getMethod();
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] values = joinPoint.getArgs();
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        try {

            StringBuffer sb = new StringBuffer();
            sb.append("[ ");

            for(int i = 0 ; i < parameterNames.length ; i ++){
                sb.append(parameterNames[i]).append(":").append(values[i])
                        .append("(").append(parameterTypes[i].getTypeName()).append(")");
                if(i != parameterNames.length - 1){
                    sb.append(" , ");
                }
            }

            sb.append(" ]");
            log.info("[|||||||] - request url  {} - ({}) params : {}",uri,requestMethod,sb.toString());

            result = joinPoint.proceed();

            if(result != null){
                log.info("[|||||||] - request url  {} - ({}) return : {}({})",uri,requestMethod,result,result.getClass().getTypeName());
            }else{
                log.info("[|||||||] - request url  {} - ({}) end.",uri,requestMethod);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        return result;
    }
}
