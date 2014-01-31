#!/bin/bash

if [ -z "$1" ]
then
    echo "Missing argument. Usage: live-git-log.sh <number of recent log lines>"
    exit 0
fi

while :
do
	clear
	git --no-pager log -$1 --graph --all --pretty=format:'%Cred%h%Creset -%C(auto)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --date=relative --date-order
	sleep 1
done
