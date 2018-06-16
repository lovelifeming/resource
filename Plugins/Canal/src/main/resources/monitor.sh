#!/bin/sh
JarName=canal.jar
   
isRunning=$(ps -ef | grep $JarName | grep -v grep)    
if [ "$isRunning" ] ; then   
    echo "$JarName is running ~~~"   
else   
    echo "$JarName 异常" | mailx -r monitor -s "$JarName ERROR" test@qq.com
fi
