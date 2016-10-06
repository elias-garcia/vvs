--------------------------------- DROP TABLES ----------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

DROP TABLE PingTable;
DROP TABLE BetInfo;
DROP TABLE TypeOption;
DROP TABLE BetType;
DROP TABLE EventInfo;
DROP TABLE CategoryInfo;
DROP TABLE UserProfile;

-------------------------------- CREATE TABLES ---------------------------------
-- Indexes for primary keys have been explicitly created.

------------- Table for validation queries from the connection pool. -----------

CREATE TABLE PingTable (foo CHAR(1));

--------------------------------- UserProfile ----------------------------------

CREATE TABLE UserProfile (
    usrId BIGINT NOT NULL AUTO_INCREMENT,
    loginName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    enPassword VARCHAR(13) NOT NULL,
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(40) NOT NULL,
    email VARCHAR(60) NOT NULL,
    CONSTRAINT UserProfile_PK PRIMARY KEY (usrId),
    CONSTRAINT LoginNameUniqueKey UNIQUE (loginName))
    ENGINE = InnoDB;

CREATE INDEX UserProfileIndexByLoginName ON UserProfile (loginName);

--------------------------------- CategoryInfo ---------------------------------

CREATE TABLE CategoryInfo (
    categoryId BIGINT NOT NULL AUTO_INCREMENT,
    categoryName VARCHAR(30) NOT NULL,
    CONSTRAINT Category_PK PRIMARY KEY (categoryId))
    ENGINE = InnoDB;

---------------------------------- EventInfo -----------------------------------

CREATE TABLE EventInfo (
    eventId BIGINT NOT NULL AUTO_INCREMENT,
    eventName VARCHAR(60) NOT NULL,
    eventDate TIMESTAMP NOT NULL,
    categoryId BIGINT,
    CONSTRAINT EventInfo_PK PRIMARY KEY (eventId),
    CONSTRAINT EventInfo_FK FOREIGN KEY (categoryId)
        REFERENCES CategoryInfo (categoryId) ON DELETE CASCADE)
    ENGINE = InnoDB;

----------------------------------- BetType ------------------------------------

CREATE TABLE BetType(
    typeId BIGINT NOT NULL AUTO_INCREMENT,
    question VARCHAR(80) NOT NULL,
    isMultiple BIT NOT NULL,
    eventId BIGINT NOT NULL,
    CONSTRAINT BetType_PK PRIMARY KEY (typeId),
    CONSTRAINT BetType_FK FOREIGN KEY (eventId)
        REFERENCES EventInfo (eventId) ON DELETE CASCADE)
    ENGINE = InnoDB;

---------------------------------- BetOption -----------------------------------

CREATE TABLE TypeOption (
    optionId BIGINT NOT NULL AUTO_INCREMENT,
    odd NUMERIC(10,2) NOT NULL,
    result VARCHAR(40) NOT NULL,
    isWinner BIT,
    version BIGINT,
    typeId BIGINT NOT NULL,
    CONSTRAINT TypeOption_PK PRIMARY KEY (optionId),
    CONSTRAINT TypeOption_FK FOREIGN KEY (typeId)
        REFERENCES BetType (typeId) ON DELETE CASCADE)
    ENGINE = InnoDB;

----------------------------------- BetInfo ------------------------------------

CREATE TABLE BetInfo (
    betId BIGINT NOT NULL AUTO_INCREMENT,
    betDate TIMESTAMP NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    userId BIGINT NOT NULL,
    optionId BIGINT NOT NULL,
    CONSTRAINT BetInfo_PK PRIMARY KEY (betId),
    CONSTRAINT BetInfoUser_FK FOREIGN KEY (userId)
        REFERENCES UserProfile (usrId) ON DELETE CASCADE,
    CONSTRAINT BetInfoOption_FK FOREIGN KEY (optionId)
        REFERENCES TypeOption (optionId) ON DELETE CASCADE)
    ENGINE = InnoDB;
