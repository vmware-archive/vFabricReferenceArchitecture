-- Copyright (c) 2013 VMware, Inc. All rights reserved. 

create schema app;

-- ----------------------------------------------------------------------- 
-- orders 
-- ----------------------------------------------------------------------- 

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


insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'buy', 'closed', 118.65, 126, '2012-08-30',  1, 32769, 'aapl', 4);
insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'buy', 'closed', 118.65, 121.43, '2012-08-30',  2, 32769, 'nvda', 5);
insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'buy', 'closed', 118.65, 95.37, '2012-08-30',  3, 32769, 'bbby', 6);
insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'buy', 'closed', 118.65, 131.04, '2012-08-30',  4, 32769, 'adsk', 7);
insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'buy', 'closed', 118.65, 115.60, '2012-08-30',  5, 32769, 'cost', 8);
insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'buy', 'closed', 118.65, 132.75, '2012-08-30',  6, 32769, 'amzn', 9);
insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'sell', 'closed', 118.65, 126, '2012-08-30',  7, 32770, 'alxn', 13);
insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'buy', 'closed', 118.65, 126, '2012-08-30',  8, 32770, 'altr', 14);
insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'buy', 'closed', 118.65, 126, '2012-08-30',  9, 32770, 'amzn', 15);
insert into app.orders (orderfee, completiondate, ordertype, orderstatus, price, quantity, opendate, orderid, account_accountid, quote_symbol, holding_holdingid)
values (10.50, '2012-08-30', 'sell', 'closed', 118.65, 126, '2012-08-30',  10, 32770, 'amgn', 16);


