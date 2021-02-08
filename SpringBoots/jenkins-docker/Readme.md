springboot集成docker实现自动化构建镜像说明
文档说明
该文档仅包括通过配置springboot、jenkins、git等实现持续构建，持续发布的功能，不包括各个组件使用细节的讲解。

环境说明
name	version	desc
spring boot	2.0.2.RELEASE	spring boot
docker	1.13.1	docker
harbor	1.7.0	镜像仓库
jenkinsci/blueocean	1.9.0	jenkins的blueocean工具
maven	3-alpine	maven
gitlab	10.2.3	gitlab
自动化构建服务器
bigdata01,bigdat02,bigdata03三台服务器，bigdata01是数据库，bigdata02是正式环境，bigdata03是测试环境

springboot配置文件
我们公司包括测试服和正式服两套服务，所以需要添加两套配置文本
在resources下面添加application-test.properties文本，内容同测试服的配置。
在resources下面添加application-prod.properties文本，内容同slave10的配置。
对于test和prod的指定通过jvm参数-Dspring.profiles.active=test,Active profiles=test
日志配置
日志文件保存地址统一为/var/log/zsm/zsm-profiles/jenkins-docker.2019-01-02.log
将镜像的/var/log/zsm/zsm-profiles挂载到宿主机上对应的目录/var/log/zsm/zsm-profiles。这样方便随时对日志监控。
对logback配置示例<property name="LOG_HOME" value="/var/log/zsm/zsm-profiles/logs" />
pom打包方式修改
在pom的build下面将打包方式修改为如下。

<properties>
        <docker.image.prefix>bigdata01.server.com/zsm.com</docker.image.prefix>
    </properties>
​
        <plugins>
            <!--这个插件是将所有的包打在一起-->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <!-- 这个插件主要是对打的包解压到dependency目录下 -->
                 <groupId>org.apache.maven.plugins</groupId>
                 <artifactId>maven-dependency-plugin</artifactId>
                 <executions>
                     <execution>
                         <id>unpack</id>
                         <phase>package</phase>
                         <goals>
                             <goal>unpack</goal>
                         </goals>
                         <configuration>
                             <artifactItems>
                                 <artifactItem>
                                     <groupId>${project.groupId}</groupId>
                                     <artifactId>${project.artifactId}</artifactId>
                                     <version>${project.version}</version>
                                 </artifactItem>
                             </artifactItems>
                         </configuration>
                     </execution>
                 </executions>
            </plugin>
            <!-- 根据 dockerfile 构建镜像的mvn插件 -->
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>dockerfile-maven-plugin</artifactId>
                <version>1.4.9</version>
                <configuration>
                    <!-- 镜像名 -->
                    <repository>${docker.image.prefix}/${project.artifactId}</repository>
                    <!-- tag：默认使用当前版本 -->
                    <tag>${project.version}</tag>
                </configuration>
            </plugin>
        </plugins>
注意：如果在build标签下面有下面的配置，需要删除或者其他处理，要保证配置文件都能够打包到jar里面

        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/**</include>
                </includes>
            </resource>
        </resources>
 

在打包的pom文件同级目录下创建Dockerfile文件
Dockerfile内容

##FROM指定依赖jdk8这个镜像，VOLUME指定其挂载目录
FROM openjdk:8-jdk-alpine
VOLUME /tmp
##将dependency下面的内容拷贝到容器中
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
##容器启动时通过PARAMS指定jvm参数
ENV PARAMS=""
## 容器启动时执行的命令
ENTRYPOINT ["sh","-c","java -cp app:app/lib/* $PARAMS com.zsm.JDApplication"]
说明

上面是一种的构建镜像的方式：这种方式是将jar解压之后分别拷贝lib和class到容器中，这种方式能够灵活的对历史版本镜像进行配置修改等操作，方便镜像在不同服务器之间的迁移。还有另外一种，直接将整个jar拷贝到容器中：这种方式构建简单，但是将配置打包到jar里面，不方便对配置的修改。com.zsm.JDApplication每个项目对应的主类不一样，需要进行修改。

dockerfile命令详解

在打包的pom文件同级目录下创建Jenkinsfile
Jenkinsfile内容

pipeline {
    parameters {
        booleanParam(name:'UPDATE_FORMAL', defaultValue:false,description:'默认false：不更新正式服; true：更新正式服')
        booleanParam(name:'NOT_SKIP_BUILD', defaultValue:true, description:'默认true：构建新的镜像;false：跳过镜像构建')
    }
    environment {
        BITBUCKET_COMMON_CREDS = credentials('docker-registry-up')
        RegistryName = 'bigdata01.server.com'
        ImageName = 'zsm.com/enterprise-image'
        ImageTag = '1.0.0'
    }
    agent any
    stages {
        stage('Build') {
            when {
                expression { return params.NOT_SKIP_BUILD }
            }
            agent {
                docker {
                    image 'maven:3-alpine'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                echo '开始mvn构建镜像...'
                sh 'cd .'
                sh 'mvn -B -DskipTests clean package dockerfile:build'
                echo '镜像构建完成!'
            }
        }
        stage('Push') {
            when {
                expression { return params.NOT_SKIP_BUILD }
            }
            steps {
                echo 'push镜像到私有仓库...'
                sh 'docker login -u $BITBUCKET_COMMON_CREDS_USR -p $BITBUCKET_COMMON_CREDS_PSW $RegistryName'
                sh 'docker tag $RegistryName/$ImageName:$ImageTag $RegistryName/$ImageName:latest'
                sh 'docker push $RegistryName/$ImageName:$ImageTag'
                sh 'docker push $RegistryName/$ImageName:latest'
                sh 'docker rmi $RegistryName/$ImageName:$ImageTag'
                sh 'docker rmi $RegistryName/$ImageName:latest'
                echo 'push镜像到私有仓库完成!'
            }
        }
        stage('Pull_Test') {
            steps {
                echo '开始更新测试服...'
                sh 'ssh root@bigdata03 "/opt/server/zsm-microservice/jenkins-remote-shell/zsm-profiles.sh"'
                echo '测试服更新完成!'
            }
        }
        stage('Pull_Formal') {
            when {
                expression { return params.UPDATE_FORMAL }
            }
            steps {
                echo '开始更新正式服...'
                sh 'ssh root@bigdata02 "/opt/server/zsm-microservice/jenkins-remote-shell/zsm-profiles.sh"'
                echo '正式服更新完成!'
            }
        }
    }
}
说明

这个文件对每次版本的变动都需要修改ImageTag为对应的版本。
注意：这个文件里面不能写注释。
对于在项目的module下打包的情况，需要把 stage('Build') 下面的steps里面的 sh 'mvn -B -DskipTests clean package dockerfile:build -f pom.xml'改为对应的module下面。
Jenkins的pipline语法有Scripted Pipeline和Declarative Pipeline两种，这里使用的是声明式pipline。pipline语法详情。
gitlab项目设置 -- （这项废弃，不用管了）
因为目前是用我的账号管理的ssh-key，所以需要把我添加为可访问的账号。
通过setting -> members -> add member的方式添加
Jenkins配置
jenkins访问地址:http://bigdata01:8080，账号密码:admin/123456
jenkins pipline创建流程: new任务 -> enter an item name -> 流水线 -> ok. /pipline -> definition -> pipline script from SCM -> SCM -> git -> Repository url -> credentials -> jenkins-gitlab-private -> 输入分支，默认master分支 -> save。
如果Jenkinsfile在module下面，请在Script Path指定对应的path
远端服务器脚本
因为镜像的pull和run都是通过Jenkins调用远程的shell脚本来执行的,所以需要编写对应的脚本。所以对于正式服和测试服的每一台服务器都需要对应的脚本。

脚本内容

#!/bin/bash
​
docker login -u admin -p Harbor12345 test03
​
docker stop zsm-profiles
​
docker rm -f zsm-profiles
​
docker rmi bigdata01.server.com/zsm.com/enterprise-image:latest
​
docker pull bigdata01.server.com/zsm.com/enterprise-image:latest
​
docker run -d -p 8100:8100 -v /etc/hosts:/etc/hosts -v /var/log/zsm/zsm-profiles:/var/log/zsm/zsm-profiles --name zsm-profiles -e PARAMS='-Dspring.profiles.active=test -Djava.security.egd=file:/dev/./urandom' --net=host bigdata01.server.com/zsm.com/enterprise-image:latest
​
说明：

停止容器和删除容器需要把名字和容器启动的名字保持一致。
删除镜像和pull镜像需要改为自己项目构建的镜像，tag使用latest不变。
运行新的镜像里面需要把-v /var/log/zsm/zsm-profiles:/var/log/zsm/zsm-profiles日志输出改为自己项目对应的地址，--name zsm-profiles也需要改成自己项目的名字（和停止删除容器的名字保持一致），-e PARAMS='-Dspring.profiles.active=test -Djava.security.egd=file:/dev/./urandom'用来指定jvm运行参数。
手动构建Jenkins pipline
进入项目对应的任务，立即构建 -> build history -> 查看构建日志。
不足
目前使用的是手动构建方式进行任务的启动，后期优化成每一个项目都在git上有对应的生产分支，jenkins只需要webhook对应的生产分支就行。
对于程序的版本和镜像打包的tag，必须保持一致才能有效，版本的迭代每次需要改动两个地方，有点麻烦。