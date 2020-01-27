package ru.sbt.javaschool;

import ru.sbt.javaschool.cache.Calculator;

public class Profiler {
    public static void profiling(Calculator calc, int start, int end) {
        long before = System.nanoTime();
        for (int i = 10; i < 30; i++) {
            System.out.println(calc.fibonacci(i));
        }
        long after = System.nanoTime();
        System.out.println("Время выполнения: " + (after - before));
    }
}
