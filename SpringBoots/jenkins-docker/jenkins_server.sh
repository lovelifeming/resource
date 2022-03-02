#!/bin/bash
#Jenkins配置shell命令
#export BUILD_ID=jenkins_server		
#sh /home/jenkins_server/jenkins_server.sh jenkins_server jenkins_server com.zsm.Appilcation 8080 jenkins_server

echo "启动Jenkins单服务开始……"
if [ $# -lt 4 ]; then
  echo echo '参数个数不够[参数格式:脚本路径如:/home/springcloud_servers/jenkins_server.sh,工作空间目录如:jenkins_server,项目名称如:jenkins_server,全类名如:com.zsm.Appilcation,端口,日志文件名称】请重新输入…'
  exit 1
fi

export JAVA_HOME=/usr/java/jdk1.8.0_201
export CLASSPATH=.:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar
export M2_HOME=/usr/maven/apache-maven-3.6.0

#执行程序启动所使用的系统用户，考虑到安全，推荐不使用root帐号  
#RUNNING_USER=developer

export PATH=$PATH:$JAVA_HOME/bin:$M2_HOME/bin
cd `dirname $0`
# 脚本所在的目录
BIN_DIR=`pwd`
echo ${BIN_DIR}
#/root/.jenkins/workspace/JenkinsServer/jenkins_server
#项目的工作空间的目录：JenkinsServer
GIT_WORKSPACE_DIR=$1
#项目名称:这里是jenkins_server
PROJECT_NAME=$2
#应用启动类的全类名
APP_MAINCLASS=$3
#服务端口：如8080
APP_PORT=$4
#日志文件名称
QSJ_BASE_LOG_FILE_NAME=$5
#存放日志的基本目录
QSJ_BASE_LOG_DIR='/home/data/logs/'
JENKINS_WORKSPACE_DIR='/root/.jenkins/workspace'
#项目的工作空间绝对路径
#'/'${GIT_WORKSPACE_DIR}
WORKSPACE=${JENKINS_WORKSPACE_DIR}
#服务部署的根目录
SERVER_DIR='/home/springcloud_servers'

#java虚拟机启动参数  
JAVA_OPTS="-Xms256m -Xmx2048m"

#去jenkins上打包的目录
cd ${WORKSPACE}'/'${PROJECT_NAME}

#执行maven打包命令
/usr/maven/apache-maven-3.6.0/bin/mvn clean -e -U
/usr/maven/apache-maven-3.6.0/bin/mvn install -Dmaven.test.skip=true -e -U
/usr/maven/apache-maven-3.6.0/bin/mvn package -Dmaven.test.skip=true -e -U

DEPLOY_DIR=${SERVER_DIR}'/'${PROJECT_NAME}
if [ ! -d "$DEPLOY_DIR" ]; 
then  
 mkdir $DEPLOY_DIR
fi
#切换到这个放项目目录下面
cd $DEPLOY_DIR

#先将这个放项目的目录下的文件全部删除
#rm -rf *
#切换到target目录下面
cd ${WORKSPACE}'/'${PROJECT_NAME}'/target'
#拷贝loan-common.jar到部署路径
cp ${WORKSPACE}'/'${PROJECT_NAME}'/target/'${PROJECT_NAME}'.jar' ${DEPLOY_DIR}

#拷贝后将打包目录下文件删除
#cd ../..
#rm -rf *

cd ${DEPLOY_DIR}
#判断端口有没有被占用
SERVER_PORT_COUNT=`netstat -tln | grep $APP_PORT | wc -l`
	if [ $SERVER_PORT_COUNT -gt 0 ]; then
		echo "The $SERVER_NAME port $APP_PORT already used!"
	fi
#先停止应用
COUNT=`ps  --no-heading -C java -f --width 1000 | grep "${APP_MAINCLASS}" | awk '{print $2}' | wc -l`
if [ $COUNT -gt 0 ]; then
	SERVER_PID=`ps  --no-heading -C java -f --width 1000 | grep ${APP_MAINCLASS} | awk '{print $2}'`
    kill -9 $SERVER_PID	
fi

echo "开始启动……!" 
#再启动应用
cd ${DEPLOY_DIR}
#mkdir logs
nohup $JAVA_HOME/bin/java ${JAVA_OPTS} -jar ${PROJECT_NAME}.jar $APP_MAINCLASS  >${QSJ_BASE_LOG_DIR}${QSJ_BASE_LOG_FILE_NAME}'/'${QSJ_BASE_LOG_FILE_NAME}.log 2>&1 &
echo "success[OK]"
for((i=1;i<=3;i++));  
do  
sleep 15
tail -n 500 ${QSJ_BASE_LOG_DIR}${QSJ_BASE_LOG_FILE_NAME}'/'${QSJ_BASE_LOG_FILE_NAME}.log
done
exit 0
