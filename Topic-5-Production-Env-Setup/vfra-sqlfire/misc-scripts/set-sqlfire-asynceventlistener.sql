
-- run this script using sqlf. 
-->sqlf run -file='create-sqlfire-dbsynchronizer.sql'
connect client 'localhost:1527';
alter table app.orders
	set  asynceventlistener(dbsynclistener);