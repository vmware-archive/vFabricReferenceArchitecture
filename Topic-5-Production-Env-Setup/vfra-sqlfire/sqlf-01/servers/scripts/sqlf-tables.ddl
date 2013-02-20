-- ----------------------------------------------------------------------- 
-- QUOTE 
-- ----------------------------------------------------------------------- 

DROP TABLE QUOTE;

-- ----------------------------------------------------------------------- 
-- ORDERS 
-- ----------------------------------------------------------------------- 

DROP TABLE ORDERS;

-- ----------------------------------------------------------------------- 
-- HOLDING 
-- ----------------------------------------------------------------------- 

DROP TABLE HOLDING;

-- ----------------------------------------------------------------------- 
-- HIBERNATE_SEQUENCES 
-- ----------------------------------------------------------------------- 

DROP TABLE HIBERNATE_SEQUENCES;

-- ----------------------------------------------------------------------- 
-- ACCOUNT 
-- ----------------------------------------------------------------------- 

DROP TABLE ACCOUNT;

-- ----------------------------------------------------------------------- 
-- ACCOUNTPROFILE 
-- ----------------------------------------------------------------------- 

DROP TABLE ACCOUNTPROFILE;


-- ----------------------------------------------------------------------- 
-- PROCEDURE CHAOSFUNCTION 
-- ----------------------------------------------------------------------- 

DROP PROCEDURE CHAOSFUNCTION;

-- ----------------------------------------------------------------------- 
-- ACCOUNTPROFILE 
-- ----------------------------------------------------------------------- 

CREATE TABLE ACCOUNTPROFILE
(
    PROFILEID INTEGER NOT NULL,
    ADDRESS VARCHAR(250),
    AUTHTOKEN VARCHAR(100),
    CREDITCARD VARCHAR(250),
    EMAIL VARCHAR(250),
    FULLNAME VARCHAR(250),
    PASSWD VARCHAR(250),
    USERID VARCHAR(250) NOT NULL,
    PRIMARY KEY (PROFILEID)
)
PERSISTENT 'PERSISTENT1' SYNCHRONOUS
REPLICATE;

CREATE UNIQUE INDEX ACCOUNTPROFILE_USERID_KEY ON ACCOUNTPROFILE (USERID);



-- ----------------------------------------------------------------------- 
-- ACCOUNT 
-- ----------------------------------------------------------------------- 

CREATE TABLE ACCOUNT
(
    ACCOUNTID INTEGER NOT NULL,
    BALANCE NUMERIC(14,2),
    CREATIONDATE DATE,
    LASTLOGIN DATE,
    LOGINCOUNT INTEGER NOT NULL,
    LOGOUTCOUNT INTEGER NOT NULL,
    OPENBALANCE NUMERIC(14,2),
    PROFILE_PROFILEID INTEGER,
    VERSION INTEGER,
    PRIMARY KEY (ACCOUNTID),
    CONSTRAINT FKE49F160D2BA34895 FOREIGN KEY (PROFILE_PROFILEID) REFERENCES ACCOUNTPROFILE (PROFILEID)
)
PERSISTENT 'PERSISTENT1' SYNCHRONOUS
REDUNDANCY 1
PARTITION BY PRIMARY KEY;


-- ----------------------------------------------------------------------- 
-- HIBERNATE_SEQUENCES 
-- ----------------------------------------------------------------------- 

CREATE TABLE HIBERNATE_SEQUENCES
(
    SEQUENCE_NAME VARCHAR(255),
    SEQUENCE_NEXT_HI_VALUE INTEGER
)
REPLICATE
PERSISTENT 'PERSISTENT1' SYNCHRONOUS;

-- ----------------------------------------------------------------------- 
-- HOLDING 
-- ----------------------------------------------------------------------- 

CREATE TABLE HOLDING
(
    HOLDINGID INTEGER NOT NULL,
    ACCOUNT_ACCOUNTID INTEGER,
    PURCHASEDATE DATE,
    PURCHASEPRICE NUMERIC(14,2),
    QUANTITY NUMERIC(14,0) NOT NULL,
    QUOTE_SYMBOL VARCHAR(250),
    PRIMARY KEY (HOLDINGID)
)
REDUNDANCY 1
PERSISTENT 'PERSISTENT1' SYNCHRONOUS
PARTITION BY COLUMN (ACCOUNT_ACCOUNTID)
COLOCATE WITH (ACCOUNT);

-- ----------------------------------------------------------------------- 
-- ORDERS 
-- ----------------------------------------------------------------------- 

CREATE TABLE ORDERS
(
    ORDERID INTEGER NOT NULL,
    COMPLETIONDATE DATE,
    OPENDATE DATE,
    ORDERFEE NUMERIC(14,2),
    ORDERSTATUS VARCHAR(250),
    ORDERTYPE VARCHAR(250),
    PRICE NUMERIC(14,2),
    QUANTITY NUMERIC(19,2) NOT NULL,
    ACCOUNT_ACCOUNTID INTEGER,
    HOLDING_HOLDINGID INTEGER,
    QUOTE_SYMBOL VARCHAR(250),
    PRIMARY KEY (ORDERID),
    CONSTRAINT FKC3DF62E518A618B8 FOREIGN KEY (ACCOUNT_ACCOUNTID) REFERENCES ACCOUNT (ACCOUNTID)
)
ASYNCEVENTLISTENER(dbsynclistener)
REDUNDANCY 1
PERSISTENT 'PERSISTENT1' SYNCHRONOUS
PARTITION BY COLUMN (ACCOUNT_ACCOUNTID)
COLOCATE WITH (ACCOUNT);

-- ----------------------------------------------------------------------- 
-- QUOTE 
-- ----------------------------------------------------------------------- 

CREATE TABLE QUOTE
(
    QUOTEID INTEGER NOT NULL,
    LOW NUMERIC(14,2),
    OPEN1 NUMERIC(14,2),
    VOLUME NUMERIC(19,2) NOT NULL,
    PRICE NUMERIC(14,2),
    HIGH NUMERIC(14,2),
    COMPANYNAME VARCHAR(250),
    SYMBOL VARCHAR(250) NOT NULL,
    CHANGE1 NUMERIC(19,2) NOT NULL,
    VERSION INTEGER,
    PRIMARY KEY (QUOTEID)
)
PERSISTENT 'PERSISTENT1' SYNCHRONOUS
REPLICATE;

CREATE UNIQUE INDEX QUOTE_SYMBOL_KEY ON QUOTE (SYMBOL);
