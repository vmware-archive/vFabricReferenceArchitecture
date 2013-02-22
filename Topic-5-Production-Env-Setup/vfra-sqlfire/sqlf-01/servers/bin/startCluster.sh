#!/bin/bash 
# Copyright (c) 2013 VMware, Inc. All rights reserved. 

ENV=`dirname $0`
. "$ENV/common.env"

if [ "x$BIND_IP" = "x${SQLFIRE_NODES[0]}" ];
then
		for i in ${SQLFIRE_LOCATORS[*]}; do
				echo "*** Starting locator at ${i}"
				sshpass -p $SQLFIRE_PASSWORD ssh $SQLFIRE_USER@${i} "cd ${GF_SCRIPTS}; 
							   . ./sqlf-locator start;
							   sleep 1 ;" &
		done
		
		echo "*** Waiting 60 seconds for the Locator(s) to start"
		sleep 60

		for i in ${SQLFIRE_NODES[*]}; do
			echo "*** Starting SQLFire Node at ${i} ----"
			sshpass -p $SQLFIRE_PASSWORD ssh $SQLFIRE_USER@${i} "cd ${GF_SCRIPTS}; 
						   . ./sqlf start;
						   sleep 1 ;" &
		done
else
    if [ "x${SQLFIRE_NODES[0]}" != "x" ];
	then
        	echo "IMPORTANT: Login to this server [ ${SQLFIRE_NODES[0]} ] to start this SQLFire CLUSTER"
       		exit 0
	fi
fi
