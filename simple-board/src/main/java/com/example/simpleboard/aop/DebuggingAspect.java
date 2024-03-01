package com.example.simpleboard.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Component;

@Aspect // AOP 클래스 선언
@Component  // IOC 컨테이너가 해당 객체를 생성 및 관리
@Slf4j
public class DebuggingAspect {
    @Pointcut("execution(* com.example.simpleboard.api.*.*(..))")   // api 패키지 안 모든 클래스 지정
    private void cut(){}

    @Before("cut()")    // 실행 시점 : 호출 전
    public void beforeLogging(JoinPoint joinPoint){
        // 입력값 가져오기
        Object args[] = joinPoint.getArgs();

        // 클래스명
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        // 메소드명
        String methodName = joinPoint.getSignature()
                .getName();

        // 입력값 로깅하기
        for(Object obj : args){
            log.info("{}#{}의 입력값 => {}", className, methodName, obj);
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")   //  실행 시점 : 호출 성공 후
    public void afterLogging(JoinPoint joinPoint,
                             Object returnObj){
        // 클래스명
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        // 메소드명
        String methodName = joinPoint.getSignature()
                .getName();

        log.info("{}#{}의 반환값 => {}", className, methodName, returnObj);

    }
}
