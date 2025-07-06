CREATE TABLE users (
    userId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userName varchar(255)
);


CREATE TABLE moneyBalance (
    userId int,
    balance BIGINT,
    FOREIGN KEY (userId) REFERENCES users(userId)
);

