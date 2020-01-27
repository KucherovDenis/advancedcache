package ru.sbt.javaschool;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.sbt.javaschool.cache.Calculator;

public class Main {

    public static void profiling(Calculator calc, int start, int end) {
        long before = System.nanoTime();
        for (int i = 10; i < 30; i++) {
            System.out.println(calc.fibonacci(i));
        }
        long after = System.nanoTime();
        System.out.println("Время выполнения: " + (after - before));
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("appcontext.xml");) {
            Calculator calc = context.getBean(Calculator.class);

            profiling(calc, 10, 30);
            profiling(calc, 10, 30);
        }
    }
}
