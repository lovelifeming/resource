# 环境：java1.8，idea，maven
#github： https://github.com/lovelifeming
Spring Boot注意事项： 
1.Mapper必须放在resource资源文件夹下面.
    IDEA对xml文件处理的方式不同. mapper.xml文件需要放置在resource这个文件夹下.
    而eclipse只要mapper接口文件与mapper.xml放置在同一平级目录就行
2.dao层映射文件注解采用Mapper
    @SpringBootApplicatoin=@Configuration+@EnableAutoConfiguration+@ComponentScan
    @SpringBootApplicatoin是用的@ComponentScan扫描的，扫描的是@Component, @Controller, @Service, @Repository等
    Mybatis自动扫描配置中，使用注解配置时，我们只要在@MapperScan中配置我们需要扫描的Mapper位置
