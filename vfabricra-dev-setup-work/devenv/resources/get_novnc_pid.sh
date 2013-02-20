#!/bin/bash
ps aux |grep noVNC |grep -v grep |grep python | awk '{ print $2 }'
