package ru.sbt.javaschool.cache.db;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class H2SpringCacheDAO implements CacheDAO {

    private static final int FIELD_ARG = 1;
    private static final int FIELD_RESULT = 2;

    private DataSource dataSource;
    private JdbcTemplate jdbc;

    public H2SpringCacheDAO() {
        this.dataSource = getDataSource();
        jdbc = new JdbcTemplate(dataSource);
        createTable();
    }

    private DataSource getDataSource() {
        DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
        managerDataSource.setDriverClassName(ConnectionSettings.DB_DRIVER);
        managerDataSource.setUrl(ConnectionSettings.DB_URL);

        return managerDataSource;
    }

    private void createTable() {
        System.out.println("Создание таблицы");
        jdbc.execute(Query.CREATE_TABLE);
    }

    @Override
    public Map<Integer, CacheEntity> load() {
        System.out.println("Загрузка данных");
        Map<Integer, CacheEntity> map = new HashMap<>();

        List<CacheEntity> listResult = jdbc.query(Query.SELECT_ALL, new RowMapper<CacheEntity>() {
            @Override
            public CacheEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                int id = resultSet.getInt("ID");
                int arg = resultSet.getInt("ARG");
                int result = resultSet.getInt("RESULT");
                return new CacheEntity(id, arg, result);
            }
        });

        listResult.forEach(e -> map.put(e.getArg(), e));
        return map;
    }

    @Override
    public void save(Map<Integer, CacheEntity> cacheMap) {
        System.out.println("Сохранение данных");

        List<CacheEntity> insertData = cacheMap.values()
                .stream()
                .filter(v -> v.getId() < 0)
                .collect(Collectors.toList());

        jdbc.batchUpdate(Query.INSERT_CACHE, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                CacheEntity entity = insertData.get(i);
                preparedStatement.setInt(FIELD_ARG, entity.getArg());
                preparedStatement.setInt(FIELD_RESULT, entity.getResult());
            }

            @Override
            public int getBatchSize() {
                return insertData.size();
            }
        });
    }
}
