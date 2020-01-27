package ru.sbt.javaschool.cache.db;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class H2JdbcCacheDAO implements CacheDAO {

    private static final int FIELD_ARG = 1;
    private static final int FIELD_RESULT = 2;

    public H2JdbcCacheDAO() {
        try {
            Class.forName(ConnectionSettings.DB_DRIVER);
            createCacheTable();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createCacheTable() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            System.out.println("Создание таблицы");
            String query = Query.CREATE_TABLE;

            st.execute(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(ConnectionSettings.DB_URL);
    }

    @Override
    public Map<Integer, CacheEntity> load() {
        Map<Integer, CacheEntity> map = new HashMap<>();
        System.out.println("Загрузка данных");
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            ResultSet iterator = st.executeQuery(Query.SELECT_ALL);

            while (iterator.next()) {
                int id = iterator.getInt("ID");
                int arg = iterator.getInt("ARG");
                int result = iterator.getInt("RESULT");

                map.put(arg, new CacheEntity(id, arg, result));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return map;
    }

    @Override
    public void save(Map<Integer, CacheEntity> cacheMap) {
        System.out.println("Сохранение данных");
        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement( Query.INSERT_CACHE)) {
            for (CacheEntity cacheEntity : cacheMap.values()) {
                if (cacheEntity.getId() < 0) {
                    st.setInt(FIELD_ARG, cacheEntity.getArg());
                    st.setInt(FIELD_RESULT, cacheEntity.getResult());
                    st.addBatch();
                }
            }
            st.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
