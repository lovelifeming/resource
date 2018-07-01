#!/bin/bash
Base_Path=$(readlink -f $(dirname $0))
OutDir=/data/logs/canal-log
start_path=bin/startup.sh
JarName=canal.jar
Log_File=output.log

$Base_Path/$start_path
if [ ! -d $OutDir ] ; then
	mkdir -p $OutDir
fi

sleep 10

isRunning=$(ps -ef | grep $JarName | grep -v grep)
if [ "$isRunning" ] ; then
    echo "$JarName is running ~~~"
else
	if [ -f $Base_Path/$JarName ] ; then
		nohup java -jar $Base_Path/$JarName >> $OutDir/$Log_File 2>&1 &
		echo "$JarName is started ~~~"
	else
	    echo "$JarName isn't exist ~~~"
	fi
fi

