#!/bin/bash 
# Copyright (c) 2013 VMware, Inc. All rights reserved. 

ENV=`dirname $0`
. "$ENV/common.env"

stopServer() {
    if [ "x${SQLFIRE_NODES[$@]}" != "x" ];
	then
		echo "*** Stopping SQLFire node at ${SQLFIRE_NODES[$@]}" 
		sshpass -p $SQLFIRE_PASSWORD ssh $SQLFIRE_USER@${SQLFIRE_NODES[$@]} "cd ${GF_SCRIPTS}; 
							   . ./sqlf stop;
							   sleep 1 ;" &
	else
		echo "*** SQLFire Node at position $@ of SQLFIRE_NODES variable has not been defined" 
		echo "SQLFIRE_NODES = ${SQLFIRE_NODES[*]}"
	fi
}

if [ "x$BIND_IP" = "x${SQLFIRE_NODES[0]}" ];
then

	case "$1" in
	 0)
		stopServer $1
		;;
	 1)
		stopServer $1
		;;
	 2)
		stopServer $1
		;;
	 3)
		stopServer $1
		;;
	 *)
		echo " "
		echo $"Usage: $0 NUMBER"
		echo "Where NUMBER: 0, 1, 2 or 3"
		echo " "
		exit 1
		;;
	esac
else
    if [ "x${SQLFIRE_NODES[0]}" != "x" ];
	then
        	echo "IMPORTANT: Login to this server [ ${SQLFIRE_NODES[0]} ] to stop this SQLFire Node"
       		exit 0
	fi
fi