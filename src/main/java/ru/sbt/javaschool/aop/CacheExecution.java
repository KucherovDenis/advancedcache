package ru.sbt.javaschool.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.sbt.javaschool.cache.Cache;
import ru.sbt.javaschool.cache.Cacheable;
import ru.sbt.javaschool.cache.FibonacciCache;
import ru.sbt.javaschool.cache.db.H2SpringCacheDAO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Method;

@Component
@Aspect
public class CacheExecution {

    private Cache<Integer> cache;

    @PostConstruct
    public void init() {
        cache = new FibonacciCache(new H2SpringCacheDAO());
        cache.init();
        System.out.println("cache load.");
    }

    @Pointcut("execution(int ru.sbt.javaschool.cache.Calculator.fibonacci(int))")
    public void cachePointCut() {

    }

    @Pointcut("@annotation(ru.sbt.javaschool.cache.Cacheable)")
    public void annotationPointCut() {

    }

    @Around(value = "cachePointCut() && annotationPointCut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed;

        Object target = joinPoint.getTarget();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Method realMethod = target.getClass().getDeclaredMethod(signatureMethod.getName(), signatureMethod.getParameterTypes());
        Cacheable cacheable = realMethod.getAnnotation(Cacheable.class);

        if (cacheable.persistent()) {
            Integer arg = (Integer) joinPoint.getArgs()[0];
            Integer cacheResult = cache.get(arg);

            if (cacheResult != null) return cacheResult;
            else {
                proceed = joinPoint.proceed();
                cache.add(arg, (Integer) proceed);
            }
        } else {
            proceed = joinPoint.proceed();
        }

        return proceed;
    }

    @PreDestroy
    public void destroy() {
        cache.save();
        System.out.println("cache saved.");
    }
}
