##FROM指定依赖jdk8这个镜像，VOLUME指定其挂载目录
FROM java:8
VOLUME /tmp
##将dependency下面的内容拷贝到容器中
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
##容器启动时通过PARAMS指定jvm参数
ENV PARAMS=""
## 容器启动时执行的命令
ENTRYPOINT ["sh","-c","java -cp app:app/lib/* $PARAMS com.zsm.flowable.FlowableApplication"]

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
#ENTRYPOINT ["java","-jar","app.jar"]