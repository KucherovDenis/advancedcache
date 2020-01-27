package ru.sbt.javaschool.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;
import ru.sbt.javaschool.cache.Cache;
import ru.sbt.javaschool.cache.Cacheable;
import ru.sbt.javaschool.cache.Calculator;
import ru.sbt.javaschool.cache.FibonacciCache;
import ru.sbt.javaschool.cache.db.H2SpringCacheDAO;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CacheableAnotationHandlerBeanPostProcessor implements BeanPostProcessor {

    private static final String CACHED_METHOD = "fibonacci";

    private Map<String, Class> beanMap = new HashMap<>();

    private Cache<Integer> cache;

    public void init() {
        cache = new FibonacciCache(new H2SpringCacheDAO());
        cache.init();
        System.out.println("cache load.");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();
        Set<Class<?>> interfaces = ClassUtils.getAllInterfacesForClassAsSet(beanClass);
        if (interfaces.contains(Calculator.class)) {
            Method method = ClassUtils.getMethod(beanClass, CACHED_METHOD, int.class);
            if (method.isAnnotationPresent(Cacheable.class)) {
                Cacheable cacheable = method.getAnnotation(Cacheable.class);
                if (cacheable != null && cacheable.persistent()) {
                    beanMap.put(beanName, beanClass);
                }
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = beanMap.get(beanName);

        if (beanClass != null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getName().equals(CACHED_METHOD)) {
                        Integer arg = (Integer) args[0];
                        Integer cacheResult = cache.get(arg);

                        if (cacheResult != null) return cacheResult;
                        else {
                            int methodResult = (int) method.invoke(bean, args);
                            cache.add(arg, methodResult);
                            return methodResult;
                        }
                    }

                    return method.invoke(bean, args);
                }
            });
        }

        return bean;
    }

    public void destroy() {
        cache.save();
        System.out.println("cache saved.");
    }
}
