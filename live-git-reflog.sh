#!/bin/bash

if [ -z "$1" ]
then
    echo "Missing argument. Usage: live-git-reflog.sh <number of recent reflog lines>"
    exit 0
fi

while :
do
	clear
	git --no-pager reflog show -$1 
sleep 1
done
