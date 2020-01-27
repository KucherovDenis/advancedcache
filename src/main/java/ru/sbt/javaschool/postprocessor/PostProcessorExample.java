package ru.sbt.javaschool.postprocessor;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.sbt.javaschool.Profiler;
import ru.sbt.javaschool.cache.Calculator;

public class PostProcessorExample {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("appcontext.xml")) {
            Calculator calc = context.getBean(Calculator.class);

            Profiler.profiling(calc, 10, 30);
            Profiler.profiling(calc, 10, 30);
        }
    }
}
