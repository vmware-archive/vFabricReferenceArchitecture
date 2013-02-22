#!/bin/bash
# Copyright (c) 2013 VMware, Inc. All rights reserved. 
ps aux |grep noVNC |grep -v grep |grep python | awk '{ print $2 }'
