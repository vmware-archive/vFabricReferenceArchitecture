#!/bin/bash 
# run as postgres user
psql -U postgres -h localhost -p 5432 -f ../scripts/batchdbstg.sql
psql -U postgres -h localhost -p 5432 -f ../scripts/batchdbrpt.sql
psql -U postgres -h localhost -p 5432 -f ../scripts/batchdbjob.sql
