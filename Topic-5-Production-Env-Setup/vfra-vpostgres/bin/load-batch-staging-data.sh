#!/bin/bash 
# loads 500K orders to staging db
# run as postgres user
# skip records 0-100k to avoid potential conflicts with inserts by DBSynchronizer
psql -U postgres -h localhost -p 5432 -d batchdbstg -f ../scripts/staging-data/load-vpostgres-staging.200k.sql
psql -U postgres -h localhost -p 5432 -d batchdbstg -f ../scripts/staging-data/load-vpostgres-staging.300k.sql
psql -U postgres -h localhost -p 5432 -d batchdbstg -f ../scripts/staging-data/load-vpostgres-staging.400k.sql
psql -U postgres -h localhost -p 5432 -d batchdbstg -f ../scripts/staging-data/load-vpostgres-staging.500k.sql
psql -U postgres -h localhost -p 5432 -d batchdbstg -f ../scripts/staging-data/load-vpostgres-staging.600k.sql