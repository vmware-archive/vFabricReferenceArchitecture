create schema reporting;

-- ----------------------------------------------------------------------- 
-- orders - some basic transformations required 
-- ----------------------------------------------------------------------- 

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

