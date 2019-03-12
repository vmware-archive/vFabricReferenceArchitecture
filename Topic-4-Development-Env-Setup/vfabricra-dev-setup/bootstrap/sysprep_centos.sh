#!/bin/bash
# Copyright (c) 2013 VMware, Inc. All rights reserved. 

# The packages listed for installation here should be available in standard Redhat / CentOS RPM mirrors, or, on the standard installation media. In particular, no other repositories will be required. 

#export http_proxy=https://proxy:3128
#export https_proxy=https://proxy:3128

yum -y install perl
yum -y install unzip
yum -y install curl
yum -y install java-1.6.0-openjdk
yum -y install java-1.6.0-openjdk-devel
yum -y install make
yum -y install gcc
yum -y install ncurses-devel
yum -y install python

# the below is required for GUI operations - browser, STS, remote desktop
# CentOS 6.x 
yum -y install tigervnc-server
# CentOS 5.x 
yum -y install vnc-server
yum -y install twm
yum -y install xterm 
yum -y install xsetroot
yum -y install firefox
