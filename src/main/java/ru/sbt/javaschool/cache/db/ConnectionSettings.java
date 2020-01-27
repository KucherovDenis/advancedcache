package ru.sbt.javaschool.cache.db;

public interface ConnectionSettings {
    String DB_DRIVER = "org.h2.Driver";
    String DB_URL = "jdbc:h2:file:~/db/cachedb";
}
