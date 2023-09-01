package com.ecommerce.techversantInfotech.Authservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@Slf4j
public class AuthAspect {
    @Before(value = "execution(* com.ecommerce.techversantInfotech.Authservice.controller.AuthController.*(..))")
    public void beforeAdvice(JoinPoint joinPoint){

        log.info("Request to " + joinPoint.getSignature()+"started at "+ new Date());
    }

    @After(value = "execution(* com.ecommerce.techversantInfotech.Authservice.controller.AuthController.*(..))")
    public void afterAdvice(JoinPoint joinPoint){

        log.info("Request to " + joinPoint.getSignature()+"ended at "+ new Date());
    }
    @Before(value = "execution(* com.ecommerce.techversantInfotech.Authservice.service.AuthServiceImpl.*(..))")
    public void beforeAdviceToService(JoinPoint joinPoint){

        log.info("Request to " + joinPoint.getSignature()+"started at "+ new Date());
    }

    @After(value = "execution(* com.ecommerce.techversantInfotech.Authservice.service.AuthServiceImpl.*(..))")
    public void afterAdviceToService(JoinPoint joinPoint){

        log.info("Request to " + joinPoint.getSignature()+"ended at "+ new Date());
    }
}
