-- Copyright (c) 2013 VMware, Inc. All rights reserved. 
--while connected to database batchdbstg

drop schema if exists app.account;

create schema app;

drop table if exists app.account;
drop table if exists app.orders;
     
create table app.account
(
    accountid integer not null,
    balance numeric(14,2),
    creationdate date,
    lastlogin date,
    logincount integer not null,
    logoutcount integer not null,
    openbalance numeric(14,2),
    profile_profileid integer,
    version integer,
    primary key (accountid)
);

create table app.orders
(
    orderid integer not null,
    completiondate date,
    opendate date,
    orderfee numeric(14,2),
    orderstatus varchar(250),
    ordertype varchar(250),
    price numeric(14,2),
    quantity numeric(19,2) not null,
    account_accountid integer,
    holding_holdingid integer,
    quote_symbol varchar(250),
    primary key (orderid)
);

alter table app.orders
 add constraint fkc3df62e518a618b8 foreign key (account_accountid) references app.account (accountid);


