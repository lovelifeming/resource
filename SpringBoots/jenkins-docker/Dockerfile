##FROM指定依赖jdk8这个镜像，VOLUME指定其挂载目录
FROM openjdk:8-jdk-alpine
MAINTAINER test
VOLUME /tmp
##将dependency下面的内容拷贝到容器中
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
##容器启动时通过PARAMS指定jvm参数
ENV PARAMS=""
## apk源
RUN data="" \
&& sed -i 's/http:\/\/dl-cdn.alpinelinux.org\/alpine\//'"http:\/\/hub-mirror.c.163.com\/alpine\/"'/g' /etc/apk/repositories
## 字体包
RUN set -xe \
&& apk --update add ttf-dejavu fontconfig
## 容器启动时执行的命令
ENTRYPOINT ["sh","-c","java -cp app:app/lib/* $PARAMS com.zsm.JDApplication"]

## 打包jar包镜像
#FROM java:8
##指定作者信息
#MAINTAINER test
##数据卷信息
#VOLUME /tmp
#
#COPY ./target/*.jar app.jar
##启动jar
#RUN sh -c 'touch app.jar'
#ENV JAVA_OPTS=""
#ENV LANG C.UTF-8
##暴露容器端口
#EXPOSE 8080
##ENTRYPOINT 为容器启动后执行的命令
#ENTRYPOINT ["java","-jar","-Xms512m","-Xmx2048m","-XX:PermSize=256m","-XX:MaxPermSize=512m","-XX:MaxNewSize=512m","app.jar"]