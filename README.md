# stocks-alarm

`stock-alarms` is a Spring Boot application that uses Alpha Vantage API to fetch information regarding stocks / stocks price history. Users can set alarms on various stocks and get email notifications when their alarm target was met based on the current stock prices (Stocks for which users set alarms to are being fetched automatically for price updates; the scheduled task is parameterized in `application.yml` for a 30 minutes recurrence ).
 
`Users`/`Alarms` are persisted into a postgres DB via flyway db migration (`src/main/resources/db.migration/V1__CreateTables.sql`) while JDBC is used for DB operations
![image](https://i.imgur.com/r0iJP5A.png)

WIP: `Thymeleaf` is used for UI rendering; currently login / registration is being handled, on top of `Spring Security` for authentification / http security configuration / password encode; A stocks keyword-based search is prepared in the Controller (for which a `Stock` Object is used), however the UI is not ready for it yet.
