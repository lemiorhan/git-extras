#!/bin/bash

if [ -z "$1" ]
then
    echo "Missing argument. Usage: git-find-repos.sh <root path to search for git repos>"
    exit 0
fi

COUNTER=0
DIRTYCOUNTER=0
SEARCHPATH=$1

echo "Searching git repositories under $SEARCHPATH"
while IFS= read -r -d $'\0' line; do
    COUNTER=$(( $COUNTER + 1 ))    
    path=$(dirname ${line})

    cd $path
    gitStatus=$(git status -uall -s)

    if [ -n "$gitStatus" ]; then
        DIRTYCOUNTER=$(( $DIRTYCOUNTER + 1 ))    
        echo "[$DIRTYCOUNTER.] $path"
    fi
done < <(find /Users/lemiorhan -name ".git" -print0)
echo "Found $DIRTYCOUNTER changed repos among $COUNTER git repositories under $SEARCHPATH"
