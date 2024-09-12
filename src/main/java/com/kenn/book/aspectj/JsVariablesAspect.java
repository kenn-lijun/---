package com.kenn.book.aspectj;

import com.kenn.book.utils.ThreadLocalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @ClassName JsVariablesAspect
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年04月02日 14:12:00
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class JsVariablesAspect {

    @Pointcut("execution(public * com.kenn.book.rule.KennSearchUtils.*(..))")
    public void jsoupMethodPointCut() {}

    @AfterReturning(pointcut = "jsoupMethodPointCut()")
    public void doAfterReturning() {
        ThreadLocalUtils.clear();
    }

    @AfterThrowing(value = "jsoupMethodPointCut()")
    public void doAfterThrowing() {
        ThreadLocalUtils.clear();
    }

}
