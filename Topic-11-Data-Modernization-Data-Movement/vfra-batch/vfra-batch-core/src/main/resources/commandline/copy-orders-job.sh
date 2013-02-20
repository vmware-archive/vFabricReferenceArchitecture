#!/bin/bash
#execute this from vfra-batch-core folder, i.e.
#shell-prompt$ src/main/resources/commandline/copy-orders-job.sh 
mvn -Dspring.profiles.active=property-datasource exec:java -Dexec.mainClass=org.springframework.batch.core.launch.support.CommandLineJobRunner -Dexec.args="CommandLine-context.xml copyOrdersJob -next"