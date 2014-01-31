#!/bin/bash

set -e

fileNameNumberUpperLimit=100
commentNumberUpperLimit=1000

function create() {
    randomNumber=$(( ( RANDOM % $fileNameNumberUpperLimit )  + 1 ))
    file="$1$randomNumber.$2"

    if [ -f $file ];
    then
        echo "$randomNumber" >> $file
        echo "Modified >> $file"
    else
        echo "$randomNumber" >> $file
        echo "Created  >> $file"
    fi
}

function isInGitRepo() {
    git rev-parse --is-inside-work-tree 2>/dev/null
    statusCode=$?
    if [ $statusCode -eq 0 ]; then
        echo 1
    else
        echo 0
    fi
}

usage() { echo "USAGE: git-generate-changes.sh -g <COMMIT TO GIT> -p <PREFIX> -c <NUMBER OF FILES> -e <EXTENSION>"; exit 1; }

prefix=
count=
extension=
togit=
while getopts ":p:c:e:g:" OPTION; do
    case "$OPTION" in
        p)
            prefix=$OPTARG            
            ;;
        c)
            count=$OPTARG
            ;;
        e)
            extension=$OPTARG
            ;;
        g)
            togit=$OPTARG
            ;;
        *)
            usage   
            exit 0
            ;;
    esac
done

shift $((OPTIND-1))

if [ -z "$prefix" ] || [ -z "$count" ] || [ -z "$extension" ]; then
    usage
fi

if [ -n "$togit" ] && [ "$togit" != "true" ] && [ "$togit" != "false" ]; then
    usage
fi

gitStatus=$(isInGitRepo)
if [ -n "$togit" ] && [ "$togit" == "true" ] && [ "$gitStatus" == true ]; then
    echo "FATAL: The current directory is not in a git repository"
    exit 0
fi

counter=1; 
while [[ $counter -le "$count" ]]; 
do
    echo ""    
    create $prefix $extension
    if [ "$togit" == "true" ]; then
        git add -A
        randomNumber=$(( ( RANDOM % $commentNumberUpperLimit )  + 1 ))
        git commit -m "Random changes #${randomNumber}"
    fi

    let "counter += 1";
done


