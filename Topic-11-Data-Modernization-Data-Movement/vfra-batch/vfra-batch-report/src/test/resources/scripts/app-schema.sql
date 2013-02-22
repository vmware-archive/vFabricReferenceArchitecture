-- Copyright (c) 2013 VMware, Inc. All rights reserved. 

create schema reporting;

create table reporting.orders
(
    order_id integer not null,
    completion_date timestamp,
    open_date timestamp,
    order_fee numeric(14,2),
    order_status varchar(250),
    order_type integer,
    price numeric(14,2),
    quantity numeric(19,2) not null,
    account_id integer,
    holding_id integer,
    quote_symbol varchar(250),
    job_time_stamp timestamp,
    primary key (order_id, job_time_stamp)
);


insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-29', 1, 'closed', 118.65, 126, '2012-08-29',  1, 32769, 'aapl', 4, '2012-10-29 10:45:09');
insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-29', 2, 'closed', 118.65, 121.43, '2012-08-29',  2, 32769, 'nvda', 5, '2012-10-29 10:45:09');
insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-29', 1, 'closed', 118.65, 95.37, '2012-08-29',  3, 32769, 'bbby', 6, '2012-10-29 10:45:09');
insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-29', 2, 'closed', 118.65, 131.04, '2012-08-29',  4, 32769, 'adsk', 7, '2012-10-29 10:45:09');
insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-30', 1, 'closed', 118.65, 115.60, '2012-08-30',  5, 32769, 'cost', 8, '2012-10-30 10:45:09');
insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-30', 1, 'closed', 118.65, 132.75, '2012-08-30',  6, 32769, 'amzn', 9, '2012-10-30 10:45:09');
insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-30', 2, 'closed', 118.65, 126, '2012-08-30',  7, 32770, 'alxn', 13, '2012-10-30 10:45:09');
insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-31', 1, 'closed', 118.65, 126, '2012-08-31',  8, 32770, 'altr', 14, '2012-10-31 10:45:09');
insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-31', 2, 'closed', 118.65, 126, '2012-08-31',  9, 32770, 'amzn', 15, '2012-10-31 10:45:09');
insert into reporting.orders (order_fee, completion_date, order_type, order_status, price, quantity, open_date, order_id, account_id, quote_symbol, holding_id, job_time_stamp)
values (10.50, '2012-08-31', 1, 'closed', 118.65, 126, '2012-08-31',  10, 32770, 'amgn', 16, '2012-10-31 10:45:09');