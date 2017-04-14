#!/usr/bin/env bash
. git-kata-functions.sh

##############################
##### SETUP
##############################

setup
create_upstream

##############################
##### USER 1
##############################

create_user 1
initial_commit 1

commit 1 "feature" "settings" "content"
commit 1 "feature" "procfile" "content"
commit 1 "master" "config" "content"
commit 1 "master" "readme" "content"
push 1 "feature"
push 1 "master"
display_log 1

##############################
##### USER 2
##############################

create_user 2
initial_commit 2
commit 2 "feature" "pom.xml" "content"
commit 2 "master" "certificate" "content"

display_log 2

display_log_upstream