CREATE TABLE IF NOT EXISTS users (
    userId UUID PRIMARY KEY NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS alarm(
    alarmId UUID PRIMARY KEY NOT NULL,
    userId UUID NOT NULL,
    stockSymbol VARCHAR(10) NOT NULL,
    targetAlarmPercentage INT,
    currentAlarmVariance INT,
    initialStockPrice FLOAT,
    currentStockPrice FLOAT,
    isActive BIT
);

