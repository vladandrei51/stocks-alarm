package com.amigoscode.demo.model;

import com.amigoscode.demo.alphavantage.apiconnector.AlphaVantageAPIConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class DataAccessService {

    private final JdbcTemplate jdbcTemplate;
    private AlphaVantageAPIConnector alphaVantageAPIConnector;

    @Autowired
    public DataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        alphaVantageAPIConnector = new AlphaVantageAPIConnector();
    }

    boolean isAlertAlreadyAdded(UUID userId, String stockSymbol) {

        String sql = "" +
                "SELECT EXISTS ( SELECT * " +
                "FROM alarm " +
                "WHERE userId = ? " +
                "AND stockSymbol = ? )";

        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{userId, stockSymbol},
                (resultSet, i) -> resultSet.getBoolean(1)
        );

    }

    List<User> selectAllUsers() {
        String sql = "" +
                "SELECT " +
                " userId, " +
                " firstName, " +
                " lastName, " +
                " email, " +
                " password " +
                "FROM users";

        return jdbcTemplate.query(sql, mapUserFromDb());
    }

    int insertUser(UUID userId, User user) {
        String sql = "" +
                "INSERT INTO users (" +
                " userId, " +
                " firstName, " +
                " lastName, " +
                " email, " +
                " password) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                userId,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    int insertAlarm(UUID alarmId, Alarm alarm) {
        String sql = "" +
                "INSERT INTO alarm (" +
                " alarmId, " +
                " userId, " +
                " stockSymbol, " +
                " targetAlarmPercentage, " +
                " currentAlarmVariance, " +
                " initialStockPrice, " +
                " currentStockPrice, " +
                " isActive) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                alarmId,
                alarm.getUserId(),
                alarm.getStockSymbol(),
                alarm.getTargetAlarmPercentage(),
                1,
                alphaVantageAPIConnector.getStockPriceIntraDay(alarm.getStockSymbol(), 5),
                alphaVantageAPIConnector.getStockPriceIntraDay(alarm.getStockSymbol(), 5),
                true
        );
    }

    @SuppressWarnings("ConstantConditions")
    boolean isEmailTaken(String email) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM users " +
                " WHERE email = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{email},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }


    private RowMapper<User> mapUserFromDb() {
        return (resultSet, i) -> {
            String userIdStr = resultSet.getString("userId");
            UUID userId = UUID.fromString(userIdStr);

            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            return new User(
                    userId,
                    firstName,
                    lastName,
                    email,
                    password
            );
        };
    }

    List<Alarm> selectAllAlarmsFromUser(UUID userId) {
        String sql = "" +
                "SELECT " +
                " alarm.userId, " +
                " alarm.alarmId, " +
                " alarm.stockSymbol, " +
                " alarm.targetAlarmPercentage," +
                " alarm.currentAlarmVariance," +
                " alarm.initialStockPrice," +
                " alarm.currentStockPrice, " +
                " alarm.isActive " +
                "FROM alarm " +
                "WHERE alarm.userId = ?";
        return jdbcTemplate.query(
                sql,
                new Object[]{userId},
                mapAlarmFromDb()
        );
    }

    private RowMapper<Alarm> mapAlarmFromDb() {
        return (resultSet, i) ->
                new Alarm(
                        UUID.fromString(resultSet.getString("alarmId")),
                        UUID.fromString(resultSet.getString("userId")),
                        resultSet.getString("stockSymbol"),
                        resultSet.getInt("targetAlarmPercentage"),
                        resultSet.getInt("currentAlarmVariance"),
                        resultSet.getFloat("initialStockPrice"),
                        resultSet.getFloat("currentStockPrice"),
                        resultSet.getBoolean("isActive")
                );
    }

    int updateAlarmActive(UUID alarmId, boolean isActive) {
        String sql = "" +
                "UPDATE alarm " +
                "SET isActive = ? " +
                "WHERE alarmId = ?";
        return jdbcTemplate.update(sql, isActive, alarmId);
    }

    int updateAlarmTargetPercentage(UUID alarmId, Integer percentage) {
        String sql = "" +
                "UPDATE alarm " +
                "SET targetAlarmPercentage = ? " +
                "WHERE alarmId = ?";
        return jdbcTemplate.update(sql, percentage, alarmId);
    }

    int updateAlarmStockSymbol(UUID alarmId, String stockSymbol) {
        String sql = "" +
                "UPDATE alarm " +
                "SET stockSymbol = ?, " +
                "initialStockPrice = ?, " +
                "currentStockPrice = ? " +
                "WHERE alarmId = ?";
        return jdbcTemplate.update(sql,
                stockSymbol,
                alphaVantageAPIConnector.getStockPriceIntraDay(stockSymbol, 5),
                alphaVantageAPIConnector.getStockPriceIntraDay(stockSymbol, 5),
                alarmId);
    }

    int updateEmail(UUID userId, String email) {
        String sql = "" +
                "UPDATE users " +
                "SET email = ? " +
                "WHERE userId = ?";
        return jdbcTemplate.update(sql, email, userId);
    }

    int updateFirstName(UUID userId, String firstName) {
        String sql = "" +
                "UPDATE users " +
                "SET first_name = ? " +
                "WHERE userId = ?";
        return jdbcTemplate.update(sql, firstName, userId);
    }

    int updateLastName(UUID userId, String lastName) {
        String sql = "" +
                "UPDATE users " +
                "SET lastName = ? " +
                "WHERE userId = ?";
        return jdbcTemplate.update(sql, lastName, userId);
    }

    @SuppressWarnings("ConstantConditions")
    boolean selectExistsEmail(UUID userId, String email) {
        String sql = "" +
                "SELECT EXISTS ( " +
                "   SELECT 1 " +
                "   FROM users " +
                "   WHERE userId <> ? " +
                "    AND email = ? " +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{userId, email},
                (resultSet, columnIndex) -> resultSet.getBoolean(1)
        );
    }

    int deleteUserById(UUID userId) {
        String sql = "" +
                "DELETE FROM users " +
                "WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    int deleteAlarmById(UUID alarmId) {
        String sql = "" +
                "DELETE FROM alarm " +
                "WHERE alarmId = ?";
        return jdbcTemplate.update(sql, alarmId);
    }

    public List<Stock> getStocksFromSearchEndpoint(String stockSymbol) {
        return alphaVantageAPIConnector.getStockListFromSearch(stockSymbol);
    }
}
