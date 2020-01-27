package ru.sbt.javaschool.cache;

public interface Cache<T> {
    void init();

    void save();

    T get(T arg);

    void add(T arg, T result);
}
