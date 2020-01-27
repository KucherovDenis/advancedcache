package ru.sbt.javaschool.cache.db;

interface Query {
   String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS CACHE ( " +
                            "ID INT AUTO_INCREMENT, " +
                            "ARG INT NOT NULL, " +
                            "RESULT INT NOT NULL)";

   String SELECT_ALL = "SELECT * FROM CACHE";

   String INSERT_CACHE = "INSERT INTO CACHE(ARG, RESULT) VALUES(?, ?)";
}
