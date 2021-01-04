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