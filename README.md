<a name="top"/>
# Git-Extras

The best way of learning Git is practicing it. While practicing, it is beneficial to monitor the index, the status,
the objects database and the working copy in some cases. A set of bash/groovy/ruby scripts are created to monitor
and analyze Git internals and collected in this repository.

## Prerequisites

The scrips in this repository are written in Bash, Groovy and Ruby and execute native Git commands. So in order to run
all the scripts, the following applications should already be installed on your machine.

* Git 1.7+
* Groovy 2+
* Ruby 2+

## Installation

Just clone this repository to a folder (e.g. `/usr/local/bin`) which is in the list of `PATH` environment variable.

## Scripts

**Bash Scripts**
* [git-find-repos](#git-find-repos)
* [git-gc-all](#git-gc-all)
* [git-generate-changes](#git-generate-changes)
* [git-practice-platform](#git-practice-platform)

**Bash Scripts for Monitoring**
* [live-findfiles](#live-findfiles)
* [live-git-index](#live-git-index)
* [live-git-log](#live-git-log)
* [live-git-reflog](#live-git-reflog)
* [live-git-status](#live-git-status)
* [live-tree](#live-tree)

**Groovy Scripts**
* [gitanalyzer](#gitanalyzer)
* [gitcatfile](#gitcatfile)

**Ruby Scripts**
* [git-wtf](#git-wtf)

## Usage

<a name="git-find-repos"/>
#### git-find-repos

```
NAME
      git-find-repos - Finds all existing git repositories

SYNOPSIS
      git-find-repos [-h | --help] [-d | --dirty] <path>

DESCRIPTION
      git-find-repos is a Bash script that searches .git folders recursively under the given folder
      path.

OPTIONS
      <path>
          Path of a folder to run the recursive search. If it is not provided, th directory that
          you are in is used for doing the search.
      -d, --dirty
          Checks the status of detected git repositories. Whenever it finds a repository having
          updates that hasn't been push to upstream (these are the updates not added yet, the
          updates added but not committed yet, and the updates committed but not pushed yet), it
          marks the repository as dirty. With this option, only the dirty repositories are printed,
          not all of them.
      -h, --help
          Prints usage information for help.

USAGE
      Typically you can search for all available git repositories. Do not provide the path if you
      want to search in your current directory.

          git-find-repos
          git-find-repos /path/to/search

      In order to run the search just the dirty repositories, use -d option.

          git-find-repos -d
          git-find-repos -d /path/to/search
          git-find-repos --dirty /path/to/search

DISCUSSIONS
      We clone git repositories to our machines. We use many different source directories to clone
      into. For instance, sometimes we clone repositories to our home folder, sometimes to our
      `/usr/local/` folder. From time to time we lose track of what we changed in these
      repositories. This script is mainly designed for searching git repositories having local
      modifications (i.e. -d option). It can also be used to list all git repositories in your
      machine to see the overall picture of what you cloned so far.

      It might be not possible to search git repositories in some directories due to missing access
      rights. On those directories, this script does not warn you.
```
[Go to top](#top)

<a name="git-gc-all"/>
#### git-gc-all

```
NAME
      git-gc-all - Removes all unreferenced objects from Git repository

SYNOPSIS
      git-gc-all

DESCRIPTION
      git-gc-all is a Bash script that detects and removes all unreferenced objects in a Git
      repository.

USAGE
      The script first sets all git configurations about expiration and threshold information to
      zero to make ´git gc´ run properly. Then it runs ´git gc´. Garbage collector runs ´git prune´
      behind the doors. The script should be executed in a git repository.

          git-gc-all

DISCUSSIONS
      The script is originally defined a stackoverflow question about forcing gc activity. For
      details, please check http://stackoverflow.com/a/14728706/366214 Normally ´git gc´ does not
      remove unreferenced objects if the threshold for max number of unreferenced objects is not
      exceeded or expiration period is not over. This script sets them to zero to force garbage
      collector to guarantee the execution of ´git gc´.
```
[Go to top](#top)

<a name="git-generate-changes"/>
#### git-generate-changes

```
NAME
      git-generate-changes - Generates random file changes and commits to Git repository

SYNOPSIS
      git-generate-changes [-h | --help] [-g | --commit] [(-p | --prefix) <prefix>]
                           [(-c | --count) <number of files>] [(-e | --extension) <extension>]

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
      If no parameters are entered, the script creates 1 file with the default prefix and extension.
      The file is created in the root of the git repository. The number in filename is randomly
      selected between 1 and 100.

          $ git-generate-changes
          temp27.txt ... created

      You can define your own file name convention and decide to commit these files as one file
      per file.

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
      This script is mainly used when you need random commits while practicing Git. It speeds up
      your exercise if your aim is trying -for instance- merge or rebase, not commit.
```
[Go to top](#top)

<a name="git-wtf"/>
#### git-wtf

```
NAME
      git-wtf - Displays the state of your repository in a readable, easy-to-scan format

SYNOPSIS
      git-wtf [branch+] [options]

DESCRIPTION
      git-wtf is a Ruby script. It displays the state of your repository in a readable, easy-to-scan
      format. It's useful for getting a summary of how a branch relates to a remote server, and for
      wrangling many topic branches.

OPTIONS
      -l, --long
          include author info and date for each commit
      -a, --all
          show all branches across all remote repos, not just those from origin
      -A, --all-commits
          show all commits, not just the first 5
      -s, --short
          don't show commits
      -k, --key
          show key
      -r, --relations
          show relation to features / integration branches
      --dump-config
          print out current configuration and exit

USAGE
      git-wtf uses some heuristics to determine which branches are integration branches, and which
      are feature branches. (Specifically, it assumes the integration branches are named "master",
      "next" and "edge".) If it guesses incorrectly, you will have to create a .git-wtfrc file.

      To start building a configuration file, run "git-wtf --dump-config > .git-wtfrc" and edit it.
      The config file is a YAML file that specifies the integration branches, any branches to
      ignore, and the max number of commits to display when --all-commits isn't used.  git-wtf will
      look for a .git-wtfrc file starting in the current directory, and recursively up to the root.

      IMPORTANT NOTE: all local branches referenced in .git-wtfrc must be prefixed with heads/, e.g.
      "heads/master". Remote branches must be of the form remotes/<remote>/<branch>.

DISCUSSIONS
      This script is originally copyrighted to William Morgan <wmorgan at the masanjin dot nets>.
      It is under terms of licensed in GNU General Public License.
```
[Go to top](#top)

<a name="gitanalyzer"/>
#### gitanalyzer

```
NAME
      gitanalyzer - Analyzes the index and object database of git repositories

SYNOPSIS
      gitanalyzer

DESCRIPTION
      gitanalyzer is a Groovy script using native Git commands. It analyzes the index (staging area)
      and the object database (i.e. .git/objects directory) and produces a report.

      The script checks all the objects in object database, compares them with the entries in index
      and detects the obsolete ones. As a result, the script creates a report listing all the blobs,
      trees, commits and tags with a mark implying the unreferenced objects.

OPTIONS
      -d, --debug
          prints debug log information on the report

USAGE
      The script should be called in a git repository.

          gitanalyzer

      The report displays all objects including files, folders, commits and tags as a list. At the
      end a summary is displayed.

          *** INDEX vs OBJECT DATABASE:
          index count     : 117597
          object count    : 117597
          obsolete count  : 0

          *** OBJECT TYPES:
          commit          : 5315
          tree            : 89948
          blob            : 22308
          tag             : 26

          *** TIMINGS:
          execution time  : 1185894.095 ms (20 min)

DISCUSSIONS
      The initial aim of this script was to see the impact of garbage collection on objects. But
      then the report extended a little bit to prepare overall picture of the index and the object
      database.

      The script runs slow for big projects due to analysis per object. We plan to improve the
      performance in the coming releases.
```
[Go to top](#top)

<a name="git-practice-platform"/>
#### git-practice-platform

```
NAME
      git-practice-platform - Creates a bare repo and multiple users for practicing git

SYNOPSIS
      git-practice-platform
          [-f | --folder <folder>]
          [-r | --repository <repository>]
          [-u | --usercount <count>]

DESCRIPTION
      git-practice-platform is a Bash script that creates a bare repository for simulating remote
      repositories, multiple clients cloning the bare repository simulating a teamwork on the
      repository.

OPTIONS
      -f | --folder <folder>
          The name of the root folder where all users clone the repository in it
      -r | --repository <repository>
          The name of the repository which is created for testing purposes
      -u | --usercount <count>
          Number of cloning users. If not defined, 2 users are created by default.
USAGE
      In order to create 3 users, the following command can be used.

          $ git-practice-platform -u 3 -f practicegit -r uberproject
          Root folder >>
              Created as /Users/user/practicegit
          Bare repository >>
              Created under server/uberproject.git
          User1 repository >>
              Created folder server/uberproject.git
              Cloned under clients/user1/uberproject.git
              Applied initial git configurations
              Created file .gitignore
          User2 repository >>
              Created folder server/uberproject.git
              Cloned under clients/user2/uberproject.git
              Applied initial git configurations
              Created file .gitignore
          User3 repository >>
              Created folder server/uberproject.git
              Cloned under clients/user3/uberproject.git
              Applied initial git configurations
              Created file .gitignore
          1 bare repository and 3 clients are created. Ready for practicing.

      And the following folder structure is created.

          practicegit/
          |-- clients
          |   |-- user1
          |   |   `-- uberproject.git
          |   |-- user2
          |   |   `-- uberproject.git
          |   `-- user3
          |       `-- uberproject.git
          `-- server
              `-- uberproject.git

DISCUSSIONS
      The best way of learning git is practicing it. We've been practicing git by creating bare
      and cloning repositories on our locals for a long time. This script automatises the whole
      create folders process and lets you initialize the practicing platform with one command.
```
[Go to top](#top)

<a name="live-findfiles"/>
#### live-findfiles

```
NAME
      live-findfiles - Continuously displays the files with their paths as a list under the current
      folder

SYNOPSIS
      live-findfiles

DESCRIPTION
      live-findfiles is a Bash script that runs find command on every second and displays the files
      with their paths (including hidden files and folders)

USAGE
      The script runs `find` command and displays the content as a list.

          live-findfiles

DISCUSSIONS
      The script can be used to monitor the contents of folders and visualize the impact of git
      commands. We use it to monitor object warehouse (.git/objects folder) while using git
      commands (add, commit and gc).
```
[Go to top](#top)

<a name="gitcatfile"/>
#### gitcatfile

```
NAME
      gitcatfile - Displays the raw content of a git object

SYNOPSIS
      gitcatfile [-e <path>]

DESCRIPTION
      gitanalyzer is a Groovy script. It inflates git objects and displays the raw content. The
      objects could be any files under `.git/objects/` directory.

OPTIONS
      -e, --extracts
          Extracts the object in the given path

USAGE
      A valid git object file should be passed to display the raw content.

          $ gitcatfile -e .git/objects/1e/8a2b3dcc1092004df16fd151334a963815f4d5
          tree 38\0
          100644 readme.txt\0 aba8d507fce75a7b9e7b98f800618695c483608b

DISCUSSIONS
      Normally we can also view the content of each object by using `git cat-file -p` command.
      However it formats the output and does not let you see the raw content. This script
      inflates the file via ZLib inflator and parses the content byte by byte. Then converts it
      into human-friendly form.

      The following the the output of git cat-file command.

      $ git cat-file -p 1e8a2b
      100644 blob aba8d507fce75a7b9e7b98f800618695c483608b	readme.txt

      And the following is the output for the same object by our script.

      $ gitcatfile -e .git/objects/1e/8a2b3dcc1092004df16fd151334a963815f4d5
      tree 38\0
      100644 readme.txt\0 aba8d507fce75a7b9e7b98f800618695c483608b

      We see null character as "\0" and 20 byte SHA-1 of referencing objects as hex string. New
      lines are added between entries to visualize better.
```
[Go to top](#top)

<a name="live-git-index"/>
#### live-git-index

```
NAME
      live-git-index - Generates random file changes and commits to Git repository

SYNOPSIS
      live-git-index [-a]

DESCRIPTION
      live-git-index is a Bash script for continuously displaying the content of the index.

OPTIONS
      -a
          Displays the content stored in index for all references. The script displays the
          content for the current branch by default.

USAGE
      If no parameters are given, the script displays the content of index for the current
      branch.

          $ live-git-index
          Current Branch with Staged Content:
          100644 266df426af79b41503d47474e8d0703ed03cfe77 0	readme.txt

      In order to display all the content for all references, use the `-a` option.

          $ live-git-index
          Current Branch with Staged Content:
          100644 266df426af79b41503d47474e8d0703ed03cfe77 0	readme.txt

          master:
          100644 blob 266df426af79b41503d47474e8d0703ed03cfe77	readme.txt

          development:
          100644 blob aba8d507fce75a7b9e7b98f800618695c483608b	readme.txt

          test:
          100644 blob 5b65f7b6a270e011bdbba46884b0d00a392e8e9f	readme.txt

      In the sample above, 3 branches show 3 different version of the same `readme.txt` file.

DISCUSSIONS
      This script can be used to display the content of the index and visualize the changes
      after each git command (i.e. add, commit, reset, etc.)
```
[Go to top](#top)

<a name="live-git-log"/>
#### live-git-log

```
NAME
      live-git-log - Continuously displays log graph

SYNOPSIS
      live-git-log [-c | --count <max number of commits>]

DESCRIPTION
      live-git-log is a Bash script that continuously displays the commit graph.

OPTIONS
      -c, --count
          Max number of commits displayed in the graph is 20 by default. You can change the
          number by using `count` option.

USAGE
      The script uses one-line format for `git log` command. It displays the logs in one of the
      most human-friendly format (i.e. colored, one line, showing graph)

          live-git-log
          live-git-log -c 10

DISCUSSIONS
      This script is one of the most used script during git practices. With the help of this
      script, you can display the changes in the commit graph whenever it is applied.
```
[Go to top](#top)

<a name="live-git-reflog"/>
#### live-git-reflog

```
NAME
      live-git-reflog - Continuously displays log graph

SYNOPSIS
      live-git-reflog [-c | --count <max number of reflog etries>]

DESCRIPTION
      live-git-reflog is a Bash script that continuously displays the entries in your reflog.

OPTIONS
      -c, --count
          Max number of reflog entries displayed is 20 by default. You can change the
          number by using `count` option.

USAGE
      The script uses one-line format for `git log` command. It displays the logs in one of the
      most human-friendly format (i.e. colored, one line, showing graph)

          live-git-log
          live-git-log -c 10

DISCUSSIONS
      This script is one of the most used script during git practices. With the help of this
      script, you can display the changes in the commit graph whenever it is applied.
```
[Go to top](#top)

<a name="live-git-status"/>
#### live-git-status

```
NAME
      live-git-status - Continuously displays the status of git repository

SYNOPSIS
      live-git-status

DESCRIPTION
      live-git-status is a Bash script that continuously runs `git status` and displays the result.

USAGE
      The script uses one-line short format for `git status` command to consume minimum space.

          live-git-status

DISCUSSIONS
      This script is used to monitor status of git repository after your changes.
```
[Go to top](#top)

<a name="live-tree"/>
#### live-tree

```
NAME
      live-tree - Continuously displays files and folders of the given path in a tree structure

SYNOPSIS
      live-tree [<path>]

DESCRIPTION
      live-tree is a Bash script that continuously displays files and folders of the given path
      in a tree structure. If path is not provided, the script uses the current path.

OPTIONS
      <path>
          Any directory to show the contents in tree format.

USAGE
      The script uses `tree`command to build the tree structure. So if tree command is not
      installed in your system, you should install it. The script shows hidden files and folders
      too.

          live-tree
          live-tree /opt/data

DISCUSSIONS
      This script is useful when continuously you want to display the contents of the folder having
      small number files and folders in it. If the contents is a lot, the terminal window might not
      display everything in one place.
```
[Go to top](#top)

### License

The programs in this repository is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation, either version
3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public
License for more details.

You can find the GNU General Public License at: [http://www.gnu.org/licenses](http://www.gnu.org/licenses)
