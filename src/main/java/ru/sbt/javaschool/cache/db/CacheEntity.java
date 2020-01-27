package ru.sbt.javaschool.cache.db;

public class CacheEntity {

    private static int CACHE_ID = 0;

    private int id;

    private int arg;
    private int result;

    public int getId() {
        return id;
    }

    public int getArg() {
        return arg;
    }

    public void setArg(int arg) {
        this.arg = arg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public CacheEntity(int id, int arg, int result) {
        this.id = id;
        this.arg = arg;
        this.result = result;
    }

    public CacheEntity(int arg, int result) {
        this.id = --CACHE_ID;
        this.arg = arg;
        this.result = result;
    }
}
