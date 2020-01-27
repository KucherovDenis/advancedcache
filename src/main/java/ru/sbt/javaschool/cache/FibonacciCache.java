package ru.sbt.javaschool.cache;

import ru.sbt.javaschool.cache.db.CacheEntity;
import ru.sbt.javaschool.cache.db.CacheDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class FibonacciCache implements Cache<Integer> {

    private Map<Integer, CacheEntity> cacheMap = new HashMap<>();

    private CacheDAO dao;

    public FibonacciCache(CacheDAO dao) {
        this.dao = dao;
    }

    @Override
    public void init() {
        Map<Integer, CacheEntity> map = dao.load();
        cacheMap.putAll(map);
    }

    @Override
    public Integer get(Integer arg) {
        CacheEntity cacheVal = cacheMap.get(arg);
        if (cacheVal != null) {
            return cacheVal.getResult();
        }
        return null;
    }

    @Override
    public void add(Integer arg, Integer result) {
        Objects.requireNonNull(arg);
        Objects.requireNonNull(result);
        if (!cacheMap.containsKey(arg)) {
            CacheEntity val = new CacheEntity(arg, result);
            cacheMap.put(arg, val);
        }
    }

    @Override
    public void save() {
        dao.save(cacheMap);
    }
}
