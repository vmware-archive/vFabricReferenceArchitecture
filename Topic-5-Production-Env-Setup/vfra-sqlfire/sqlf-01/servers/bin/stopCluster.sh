#!/bin/bash 

ENV=`dirname $0`
. "$ENV/common.env"

if [ "x$BIND_IP" = "x${SQLFIRE_NODES[0]}" ];
then

	echo " "
	echo "Stopping SQLFire cluster..."
	echo " "
	$GF_BIN shut-down-all -locators=$LOCATORS -bind-address=$BIND_IP
	
else
    if [ "x${SQLFIRE_NODES[0]}" != "x" ];
	then
        	echo "IMPORTANT: Login to this server [ ${SQLFIRE_NODES[0]} ] to stop this SQLFire CLUSTER"
       		exit 0
	fi
fi
