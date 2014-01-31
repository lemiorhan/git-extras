# Git-Extras

Additional scripts required for monitoring and analyzing git.

## Prerequisites

The scrips in this repository are written in Bash, Groovy and Ruby and execute native Git commands. So in order to run
all the scripts, the following applications should already be installed on your machine.

* Git 1.8+
* Groovy 2+
* Ruby 2+

## Installation

Just clone this repository to a folder (e.g. `/usr/local/bin`) which is in the list of `PATH` environment variable.

## Scripts

* [git-find-repos](#git-find-repos)
* [git-gc-all](#git-gc-all)
* [git-generate-changes](#git-generate-changes)
* [git-wtf](#git-wtf)
* [GitAnalyzer](#GitAnalyzer)
* [live-findfiles](#live-findfiles)
* [live-git-index](#live-git-index)
* [live-git-log](#live-git-log)
* [live-git-reflog](#live-git-reflog)
* [live-git-status](#live-git-status)
* [live-tree](#live-tree)

## Usage

<a name="git-find-repos"/>
#### git-find-repos

```
NAME
      git-find-repos - Finds all existing git repositories

SYNOPSIS
      git-find-repos [-h | --help] [-d | --dirty] <path>

DESCRIPTION
      git-find-repos is a Bash script that searches .git folders recursively under the given folder path.

OPTIONS
      <path>
          Path of a folder to run the recursive search. If it is not provided, th directory that you are in is used for
          doing the search.
      -d, --dirty
          Checks the status of detected git repositories. Whenever it finds a repository having updates that hasn't been
          push to upstream (these are the updates not added yet, the updates added but not committed yet, and the
          updates committed but not pushed yet), it marks the repository as dirty. With this option, only the dirty
          repositories are printed, not all of them.
      -h, --help
          Prints usage information for help.

USAGE
      Typically you can search for all available git repositories. Do not provide the path if you want to search in
      your current directory.

          git-find-repos
          git-find-repos /path/to/search

      In order to run the search just the dirty repositories, use -d option.

          git-find-repos -d
          git-find-repos -d /path/to/search
          git-find-repos --dirty /path/to/search

DISCUSSIONS
      We clone git repositories to our machines. We use many different source directories to clone into. For instance,
      sometimes we clone repositories to our home folder, sometimes to our `/usr/local/` folder. From time to
      time we lose track of what we changed in these repositories. This script is mainly designed for searching git
      repositories having local modifications (i.e. -d option). It can also be used to list all git repositories in your
      machine to see the overall picture of what you cloned so far.

      It might be not possible to search git repositories in some directories due to missing access rights. On those
      directories, this script does not warn you.
```

<a name="git-gc-all"/>
#### git-gc-all

```
NAME
      git-gc-all - Removes all unreferenced objects from Git repository

SYNOPSIS
      git-gc-all

DESCRIPTION
      git-gc-all is a Bash script that detects and removes all unreferenced objects in a Git repository.

USAGE
      The script first sets all git configurations about expiration and threshold information to zero to make ´git gc´
      run properly. Then it runs ´git gc´. Garbage collector runs ´git prune´ behind the doors. The script should be
      executed in a git repository.

          git-gc-all

DISCUSSIONS
      The script is originally defined a [stackoverflow question](http://stackoverflow.com/a/14728706/366214). Normally
      ´git gc´ does not remove unreferenced objects if the threshold for max number of unreferenced objects is not
      exceeded or expiration period is not over. This script sets them to zero to force garbage collector to guarantee
      the execution of ´git gc´.
```

<a name="git-generate-changes"/>
#### git-generate-changes

```
NAME
      git-generate-changes - Generates random file changes and commits to Git repository

SYNOPSIS
      git-generate-changes [-h | --help] [-g | --commit] [(-p | --prefix) <prefix>]
                           [(-c | --count) <number of files>] [(-e | --extension) <extension>]<path>

DESCRIPTION
      git-generate-changes is a Bash script for creating bulk random commits in repository.

OPTIONS
      -g, --commit
          Commit all newly created files to the repository as one file per commit.
      -p, --prefix
          The prefix of the filename used in the new files. If not given, "temp" is used.
      -c, --count
          Number of files to create. If not given, count is set to 1.
      -e, --extension
          The extension of the filename used in new files. If not given, "txt" is used.
      -h, --help
          Prints usage information for help.

USAGE
      If no parameters are entered, the script creates 1 file with the default prefix and extension. The file is
      created in the root of the git repository. The number in filename is randomly selected between 1 and 100.

          $ git-generate-changes
          temp27.txt ... created

      You can define your own file name convention and decide to commit these files as one file per file.

          $ git-generate-changes -g -p catalina -e log -c 3
          catalina84.log ... created, committed
          catalina17.log ... created, committed
          catalina19.log ... created, committed

      If the random file name already exists in the same directory, the script updates it.

          $ git-generate-changes -g -p catalina -e log -c 3
          catalina63.log ... created, committed
          catalina17.log ... modified, committed
          catalina19.log ... modified, committed

DISCUSSIONS
      This script is mainly used when you need random commits while practicing Git. It speeds up your exercise
      if your aim is trying -for instance- merge or rebase, not commit.
```