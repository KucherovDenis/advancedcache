package ru.sbt.javaschool.impl;

import ru.sbt.javaschool.cache.Cacheable;
import ru.sbt.javaschool.cache.Calculator;

public class CalculatorImpl implements Calculator {

    @Override
    @Cacheable(persistent = true)
    public int fibonacci(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return fibonacci(n - 2) + fibonacci(n - 1);
        }
    }
}
