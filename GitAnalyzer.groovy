#!/usr/bin/env groovy

import java.text.SimpleDateFormat

path = new File(".").getAbsolutePath()
isDebug = false

def cli = new CliBuilder(usage: 'GitAnalyzer.groovy [-p PATH] [-d]')
detectArguments(cli, args)

stats = [:]
root = new File(path)
validTypes = ["blob", "tree", "commit", "tag"]

// ============================
// CHECKING VALID GIT REPO
// ============================

log("\n***** ANALYSIS *****\n\n")
assertGitRepo(cli, path)

// ============================
// UNPACKING OBJECTS
// ============================

log("Unpacking any packed objects ... ")

def packsDir = new File(path + "/.git/objects/pack")
def newPackDir = new File(path + "/pack_" + UUID.randomUUID().toString())
packsDir.renameTo(newPackDir)
runBash("git unpack-objects < ${newPackDir.name}/*.pack")
newPackDir.deleteDir()

log("OK")

// ============================
// ANALYZE
// ============================

analyze()
display()

// ============================
// METHODS
// ============================

def analyze() {
    execStartTime = System.nanoTime()

    // CHECKING INDEX

    revsMap = [:]

    log("Checking all revisions of all objects in index ... ")

    "git rev-list --objects --all".execute(null, root).text.eachLine {
        def tokens = it.tokenize()
        revsMap[tokens[0]] = tokens[1]
        count("index count")
    }
    log("OK")

    // ANALYZING OBJECTS DATABASE

    def objectsDir = new File(path + "/.git/objects")
    objectsMap = [:]

    log("Analyzing each object in object warehouse and index ... ")

    objectsDir.eachDir { dir ->
        dir.eachFile { file ->
            def id = dir.name + file.name
            objectsMap[id] = [:]

            def type = "git cat-file $id -t".execute(null, root).text.replace("\n", "")
            objectsMap[id]["type"] = type
            objectsMap[id]["filePath"] = (
                    revsMap[id] ?: (type == "tree" ? "[ROOT]" : "")
            )
            objectsMap[id]["id"] = id

            if (type == "commit") {
                def commitData = runBash("git show -s --format=\"%ci|%cn|%s\" $id").text.replace("\n", " ").trim()
                def commitToken = commitData.tokenize("|")
                objectsMap[id]["commitDate"] = commitToken[0]
                objectsMap[id]["committer"] = commitToken[1]
                objectsMap[id]["commitMessage"] = commitToken[2]
            }

            if (type && !revsMap.containsKey(id)) {
                objectsMap[id]["obsolete"] = true
                count("obsolete")
            }

            if (validTypes.contains(type)) {
                count(type)
                count("object count")
            }
        }
    }
    log("OK")
}

def display() {
    // DISPLAY COMMITS

    println "\n***** COMMITS *****\n"
    objectsMap.sort { it.value.commitDate }.each { String id, vals ->
        if (vals.type == "commit") {
            printf "%-3s %.6s => [%s] %20s --- %s\n", markObsoletes(vals), id.substring(0, 8), format(vals.commitDate), vals.committer, vals.commitMessage
        }
    }

    // DISPLAY OBJECTS

    println "\n***** TREES - BLOBS - TAGS *****\n"
    objectsMap.sort { it.value.filePath }.each { String id, vals ->
        if (vals.type != "commit" && !vals.id.startsWith("infopa")) {
            printf "%-3s %.6s => %-8s %s\n", markObsoletes(vals), id.substring(0, 8), vals.type, vals.filePath
        }
    }

    // DISPLAY STATS

    println "\n***** STATS *****"
    stats["execution time"] = "${((double) System.nanoTime() - execStartTime) / 1000000.0} ms"
    println "\n*** INDEX vs OBJECT DATABASE:"
    printf "%-15s : %s\n", "index count", stats["index count"] ?: 0
    printf "%-15s : %s\n", "object count", stats["object count"] ?: 0
    printf "%-15s : %s\n", "obsolete count", stats["obsolete"] ?: 0
    println "\n*** OBJECT TYPES:"
    printf "%-15s : %s\n", "commit", stats["commit"] ?: 0
    printf "%-15s : %s\n", "tree", stats["tree"] ?: 0
    printf "%-15s : %s\n", "blob", stats["blob"] ?: 0
    printf "%-15s : %s\n", "tag", stats["tag"] ?: 0
    println "\n*** TIMINGS:"
    printf "%-15s : %s\n", "execution time", stats["execution time"]
}

def runBash(cmd) {
    def env = System.getenv();
    def envList = [];
    env.each() { k, v -> envList.push("$k=$v") }
    proc = ["bash", "-c", cmd].execute(envList, root);
    proc.waitFor()
    return proc
}

def markObsoletes(vals) {
    if (vals.obsolete) return "* "
    return ""
}

def count(name) {
    stats["$name"] = (stats["$name"] ?: 0) + 1
}

def format(val) {
    if (val instanceof Date) {
        def format = new SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(val);
    }
    return val
}

def detectArguments(cli, args) {
    cli.with {
        h longOpt: 'help', 'Show usage information'
        p longOpt: 'path', args: 1, argName: 'path', 'Analyzes internal structure of git repository in "path"'
        d longOpt: 'isDebug', 'Show debug information'
    }

    def options = cli.parse(args)
    if (!options) {
        return
    }
    if (options.h) {
        cli.usage()
        return
    }
    if (options.path) {
        path = options.path
    }
    if (options.isDebug) {
        isDebug = true
    }
}

def assertGitRepo(cli, pathOfRepo) {
    log("Checking if the given path is a valid git repository ... ")
    if (!new File(pathOfRepo + "/.git").exists()) {
        println "[ERROR] $pathOfRepo is not a valid git repository"
        cli.usage()
        System.exit(0)
    }
    log("OK")
}

startTime = 0

def log(msg) {
    if (isDebug) {
        if (msg != "OK") {
            startTime = System.nanoTime()
            print msg
        } else {
            println "$msg [in ${((double) System.nanoTime() - startTime) / 1000000.0} ms]"
            startTime = 0
        }
    }
}


