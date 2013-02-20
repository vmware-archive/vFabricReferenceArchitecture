To try out admin app in STS:
-----------------------------
1. Update batch-default.properties to use H2 settings
2. Run the SuccessfulStepTest from the core project (which will create H2 db and some initial data that will persist after test because DB_CLOSE_ON_EXIT=FALSE is used in H2 connection URL)
3. Deploy vfra-batch-admin to STS tc Server instance
4. In browser, open http://localhost:8080/vfra-batch-admin
