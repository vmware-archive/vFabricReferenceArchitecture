#!/bin/bash
# Copyright (c) 2013 VMware, Inc. All rights reserved. 
#set -x

TARGET_DIR=/home/vfabric/vfra

export http_proxy=https://proxy:3128
export https_proxy=https://proxy:3128
export REMOTE_REPO=https://repo_server/repo
export USER_PASS=username:password

mkdir -p $TARGET_DIR

cd $TARGET_DIR
VFRA_UNZIP=${TARGET_DIR}/vFabricRA-master
ZIP_URL=https://github.com/vFabric/vFabricRA/archive/master.zip
TARGET_ZIP=${TARGET_DIR}/vFabricRA.zip
VFRA=${TARGET_DIR}/vFabricRA-master/vfra-build

echo Checking existence of $VFRA_UNZIP
if [ ! -d "${VFRA_UNZIP}" ]; then
    echo Getting $ZIP_URL
    curl -sL --user "$USER_PASS" $ZIP_URL > ${TARGET_ZIP}
    echo Unzipping $TARGET_ZIP
    unzip $TARGET_ZIP
fi

echo Entering ${VFRA}
cd ${VFRA}

#echo Running sysprep_centos.sh - this assumes that we are on a CentOS / RHEL style system. 
#(sudo sysprep_centos.sh)

echo Sourcing environment, which will retrieve tools
source env_tools.sh

M2_SETTINGS_FILE=$TARGET_DIR/settings.xml
cat > ${M2_SETTINGS_FILE} <<EOL
<settings>
	<proxies>
		<proxy>
			<protocol>http</protocol>
			<host>proxy.vmware.com</host>
			<port>3128</port>
			<nonProxyHosts>localhost</nonProxyHosts>
      	</proxy>
  	</proxies>
</settings>
EOL

ANT_COMMAND="ant -f build_ci.xml autobuild_linux -Dhttp.proxyHost=10.16.67.187 -Dhttp.proxyPort=3128 -Dm2.settingsFile=${M2_SETTINGS_FILE} -Dremote.repo=${REMOTE_REPO}"
echo Exec-ing Ant: $ANT_COMMAND
$ANT_COMMAND
