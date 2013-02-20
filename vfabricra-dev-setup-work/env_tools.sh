#!/bin/sh
# This script attempts to save time by retrieving and configuring local tools for the 
# vFabric-RA project development setup. 
#
# fetchtools.sh 

# uncomment if within a VMware lab environment.
#export http_proxy=http://proxy:3128
#export https_proxy=http://proxy:3128
#export ANT_OPTS="-Dhttp.proxyHost=proxy -Dhttp.proxyPort=3128"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

TOOLS=$DIR/tools

if [ ! -d "${TOOLS}" ]; then
	mkdir ${TOOLS}
fi

# Setup JAVA_HOME.
OPENJDK=/usr/lib/jvm/java-openjdk
if [ -d "${OPENJDK}" ]; then
	export JAVA_HOME=${OPENJDK}
fi

# Default to Sun JDK if its installed over the openJDK. 
SUN_JDK=/usr/java/latest
if [ -d "${SUN_JDK}" ]; then
	export JAVA_HOME=${SUN_JDK}
	export PATH=${SUN_JDK}/bin:${PATH}
fi

# ANT 
ANT_REMOTE=http://apache.mirrors.tds.net/ant/binaries
ANT_PKG=apache-ant-1.8.4
ANT_ARCHIVE=$ANT_PKG-bin.tar.gz
ANT_INSTALL_DIR=$TOOLS/$ANT_PKG
if [ ! -d "$ANT_INSTALL_DIR" ]; then
	echo Fetching Apache Ant @ $ANT_REMOTE/$ANT_ARCHIVE
#	wget -P $TOOLS $ANT_REMOTE/$ANT_ARCHIVE
	curl -sL $ANT_REMOTE/$ANT_ARCHIVE > $TOOLS/$ANT_ARCHIVE
	tar zxvf $TOOLS/$ANT_ARCHIVE -C $TOOLS
fi
export ANT_HOME=${TOOLS}/$ANT_PKG
export PATH=${ANT_HOME}/bin:${PATH}

# Optional but not needed

# MAVEN 
#M2_REMOTE=http://apache.mirrors.tds.net/maven/maven-3/3.0.4/binaries
#M2_PKG=apache-maven-3.0.4
#M2_ARCHIVE=$M2_PKG-bin.tar.gz
#M2_INSTALL_DIR=$TOOLS/$M2_PKG
#if [ ! -d "$M2_INSTALL_DIR" ]; then
#	wget -P $TOOLS $M2_REMOTE/$M2_ARCHIVE
#	tar zxvf $TOOLS/$M2_ARCHIVE -C $TOOLS
#fi
#export M2_HOME=${TOOLS}/$M2_PKG
#export PATH=${M2_HOME}/bin:${PATH}

# http://dist.groovy.codehaus.org/distributions/groovy-binary-2.0.4.zip 
# Groovy 
#GROOVY_REMOTE=http://dist.groovy.codehaus.org/distributions
#GROOVY_PKG=groovy-binary-2.0.4
#GROOVY_ARCHIVE=$GROOVY_PKG.zip
#GROOVY_INSTALL_DIR=$TOOLS/${GROOVY_PKG/binary-/}
#if [ ! -d "$GROOVY_INSTALL_DIR" ]; then
#	wget -P $TOOLS $GROOVY_REMOTE/$GROOVY_ARCHIVE
#	unzip $TOOLS/$GROOVY_ARCHIVE -d $TOOLS
#fi
#export GROOVY_HOME=${GROOVY_INSTALL_DIR}
#export PATH=${GROOVY_HOME}/bin:${PATH}

