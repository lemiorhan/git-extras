#!/usr/bin/env bash

set -e

number=
root=

function commit() {
    goto_user $1
    local exists=`git show-ref refs/heads/$2`
    if [ ! -n "$exists" ]; then
        git branch $2
        echo "New $2 branch created"
    fi
    checkout $1 $2
    echo "$4" >> $3
    git add $3
    git commit -m "creates $3" >/dev/null 2>&1
    echo "$2 > commit created having \"$3\" file for user $1"
}

function initial_commit() {
    goto_user $1
    echo "initial commit" >> file
    git add file
    git commit -m "initial commit" >/dev/null 2>&1
    echo "master > Initial commit created for user $1"
}

function checkout() {
    goto_user $1
    git checkout $2 >/dev/null 2>&1
}

function goto_user {
    cd "$root/repo/user$1.git"
}

function display_log() {
    goto_user $1
    checkout $1 "master"
    echo "============ CREATED COMMIT GRAPH (USER $1) ==============="
    git log --branches --remotes --tags --graph --pretty=format:'%Cred%h%Creset -%C(auto)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --date=relative
    echo "==========================================================="
}

function display_log_upstream() {
    cd "$root/server/upstream.git"
    echo ""
    echo "============ CREATED COMMIT GRAPH (UPSTREAM) ==============="
    git log --branches --remotes --tags --graph --pretty=format:'%Cred%h%Creset -%C(auto)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --date=relative
    echo "==========================================================="
}

function setup() {
    DIRECTORY="git-kata$number"

    if [ -d "$DIRECTORY" ]
    then
        rm -rf git-kata1
        echo "$DIRECTORY folder exists. Deleted."
    fi
    mkdir "git-kata$number"
    echo "New $DIRECTORY folder created"

    cd "$DIRECTORY"
    root=$(pwd)
}

function create_upstream() {
    mkdir -p "$root/server/upstream.git" > /dev/null 2>&1
    cd "$root/server/upstream.git"
    git init --bare > /dev/null 2>&1
    echo "Upstream repository created"
}

function create_user() {
    mkdir -p "$root/repo/user$1.git" > /dev/null 2>&1
    goto_user $1
    git init
    git config user.name "User${1}"
    git config user.email "user${1}@iyzico.com"
    git remote add origin "file://$root/server/upstream.git"
}

function push() {
    goto_user $1
    git push -u origin $2 >/dev/null 2>&1
    echo "Pushed commits to \"$2\" branch of upstream for user $1"
}

function usage() {
    echo "USAGE: git-kata.sh -n <KATA NUMBER>"; exit 1;
}

realargs="$@"
while [ $# -gt 0 ]; do
    case "$1" in
      -h | --help)
        usage
        ;;
      -n | --number)
        number=$2
        shift
        ;;
      *)
        usage
        break
        ;;
    esac
    shift
done
set -- $realargs

if [ ! -n "$number" ]; then
    usage
fi
