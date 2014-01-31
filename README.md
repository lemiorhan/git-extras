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
```
