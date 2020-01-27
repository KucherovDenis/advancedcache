package ru.sbt.javaschool.cache.db;

import java.util.Map;

public interface CacheDAO {
    Map<Integer, CacheEntity> load();

    void save(Map<Integer, CacheEntity> cacheMap);
}
