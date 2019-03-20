-- Copyright (c) 2013 VMware, Inc. All rights reserved. 

-- Step 1: create asyncheventlistener
-- Prerequisites:
-- 1. Ensure 'server groups' ('dbsync' used below) matches -server-groups used by servers (not necessary for locator)
-- 2. Download postgresql driver https://jdbc.postgresql.org/download/postgresql-9.2-1000.jdbc4.jar to /opt/postgresql. If you change this location,
-- you must update CLASSPATH variable used by ../bin scripts
-->3. Set Postgresql URL to the database where SQLFire will write to
--
drop asynceventlistener dbsynclistener;
create asynceventlistener dbsynclistener
(
listenerclass 'com.vmware.sqlfire.callbacks.DBSynchronizer'
initparams
   'org.postgresql.Driver,jdbc:postgresql://postgres:5432/batchdbstg?user=postgres&password=postgres' 
enablepersistence true
diskstorename asyncevent1
)
server groups (dbsync);
call sys.start_async_event_listener('DBSYNCLISTENER');