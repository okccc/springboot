## SpringBoot
```shell
# 系统要求：jdk17、maven3.6.3、servlet6、tomcat10

# 开发步骤：创建项目 - pom.xml导入启动器 - application.yml修改配置项 - 启动类 - 业务类

# 启动器：https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.build-systems.starters
# 配置项：https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties

# 官方提供的启动器：spring-boot-starter-xxx
# 第三方提供启动器：xxx-spring-boot-starter

# 简化整合：导入starter就会自动整合相关功能
# 简化配置：application.yml集中管理spring/springmvc/mybatis/datasource/redis等所有配置
# 简化部署：以前web项目是打成war包部署到tomcat,现在只要有java环境就行,java -jar会触发内置tomcat运行
# 简化运维：jar包外部放一个application.yml文件即可

# 依赖管理
# 启动器会包含一组依赖,按照maven依赖的传递原则A-B-C,导入启动器也会把这些依赖都导进来
# 比如导入starter-web会自导入该场景下的所有依赖starter-tomcat、starter-json、spring-web、spring-webmvc
# boot项目 - spring-boot-starter-parent - spring-boot-dependencies 声明了大部分常见jar包的依赖版本
# 父项目管理所有依赖的版本,保证依赖之间的兼容性和稳定性,当然也可以自定义版本将其覆盖,父项目没有的第三方jar包要单独引入

# 自动配置
# starter-xxx - spring-boot-starter - spring-boot-autoconfigure囊括了所有场景启动器的配置类
# - XxxAutoConfiguration配置类 - @Bean注册组件 - XxxProperties属性类 - application.yml配置项
# 1.导入starter就会导入spring-boot-autoconfigure包
# 2.META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports指定了项目启动时要加载的所有自动配置类
# 3.@EnableAutoConfiguration会把这些自动配置类都导入进来,并根据条件注解按需加载
# 4.XxxAutoConfiguration通过@Bean/@Import往容器中注册组件,这些组件从XxxProperties提取属性值
# 5.XxxProperties又是和application.yml绑定,所以导入starter后只需修改配置项就能修改组件底层行为
```

## 自动配置源码分析
```shell
@SpringBootApplication
# @SpringBootConfiguration：标记为配置类,代替配置文件,点进去发现就是@Configuration
# @EnableAutoConfiguration：开启自动配置(核心),包含两部分
# @ComponentScan：排除前面已经扫描过的@Configuration和@AutoConfiguration,避免重复扫描
# 多个注解的执行顺序是由内到外,debug时断点顺序如下：
# AutoConfigurationExcludeFilter(50行) - AutoConfigurationImportSelector(126行) - AutoConfigurationPackages.Registrar(128行)

@EnableAutoConfiguration
# 1.加载主程序包下自定义的组件
# @EnableAutoConfiguration - @AutoConfigurationPackage - @Import(AutoConfigurationPackages.Registrar.class)
# Registrar源码128行打断点debug运行 - 选中new PackageImports(metadata).getPackageNames()
# 右键 - Evaluate Expression - Evaluate,发现result="com.okccc",这就解释了为啥SpringBoot默认只扫描主程序所在包及其子包

# 2.加载autoconfigure包下各个starter引入的组件
# @EnableAutoConfiguration - @Import(AutoConfigurationImportSelector.class)
# AutoConfigurationImportSelector源码126行打断点debug运行 - StepInto进入load方法 - 继续StepInto并选中load - 往下StepOver
# 发现location="META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports"
# 项目启动时会加载该文件中的所有配置类,但并不会全部生效,因为每个配置类都添加了条件注解,查看源码发现报红说明缺少相关启动器依赖,按需加载即可
# 这些配置类会往容器中放很多组件,生成一些默认功能,实际开发过程中主要就是分析这些组件的功能,如果不满意也可以自定义

@EnableConfigurationProperties
# XxxAutoConfiguration - 直接搜索@EnableConfigurationProperties - XxxProperties
# EmbeddedWebServerFactoryCustomizerAutoConfiguration - ServerProperties - prefix = "server"
# WebMvcAutoConfiguration - WebMvcProperties & WebProperties - "spring.web"
# DispatcherServletAutoConfiguration - WebMvcProperties - prefix = "spring.mvc"
# ThymeleafAutoConfiguration - ThymeleafProperties - prefix = "spring.thymeleaf"
# MessageSourceAutoConfiguration - prefix = "spring.messages"
# DataSourceAutoConfiguration - DataSourceProperties - prefix = "spring.datasource"
# DruidDataSourceAutoConfigure - DruidStatProperties & DataSourceProperties - "spring.datasource.druid"
# RedisAutoConfiguration - RedisProperties - prefix = "spring.data.redis"
# ElasticsearchRestClientAutoConfiguration - ElasticsearchProperties - prefix = "spring.elasticsearch"
# MybatisAutoConfiguration - MybatisProperties - prefix = "mybatis"

# SPI(Service Provider Interface)：是一种软件设计思想,用于在应用程序中动态地发现和加载组件
# SpringBoot的spi文件：META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
# SpringBoot项目启动时会去找这个spi文件,里面包含了各个场景要加载的自动配置类,可以随时添加或删除,方便springboot版本升级

# 功能开关
# 自动配置：项目启动时会自动加载spi文件指定的所有配置类
# 手动控制：@EnableXxx开启相关功能,比如@EnableWebMvc/@EnableAsync/@EnableCaching/@EnbaleKafka都是利用@Import导入该功能需要的组件
```

## 整合日志
```shell
# 详见 logback-spring.xml
# starter-xxx - spring-boot-starter - spring-boot-starter-logging
# 日志默认使用slf4j + logback组合,但底层实现是开放的,可对接其他日志框架,详见spring-jcl包下的LogAdapter
# 日志是系统刚启动时就使用,XxxAutoConfiguration是系统启动之后注册组件时使用
# 日志是利用监听器ApplicationListener配置的
# 日志所有配置都以"logging"开头,参照spring-boot包下的additional-spring-configuration-metadata.json文件
#
# 日志格式：时间 - 日志级别 - 进程id - 消息分隔符 - [线程名] - 产生日志的类 - 日志主体内容
# 记录日志：Logger logger = LoggerFactory.getLogger(getClass()); 或者使用Lombok的@Slf4j注解
# 日志分组：比如tomcat相关的日志统一设置,springboot预定义了web和sql两个分组
# 文件输出：默认控制台打印,也可以输出到指定文件
# 归档切割：每天的日志单独存档,超过10M就切割成另一个文件
# 外部文件：除了在application.yml配置外,也可以自定义logback-spring.xml/log4j2-spring.xml
# 切换日志：如果不想使用默认的logback,可以利用maven依赖的就近原则,先将默认依赖排除,再引入要用的依赖
#
# 为什么是logback-spring.xml而不是logback.xml
# logback框架能识别logback.xml但识别不了logback-spring.xml,这样就只能被springboot解析,也就可以使用springProfile高级功能
#
# springboot(slf4j + logback)、spring(jcl)、hibernate(jboss-logging)、mybatis...
# 每个框架的日志实现都不一样,比如spring缺少jcl会报错,springboot会将系统中所有框架的日志都统一到slf4j
# springboot适配所有日志框架,引入其它框架时先排除日志依赖,springboot提供了对应的桥接包代替 https://www.slf4j.org/legacy.html
#
# 使用步骤：
# 1.导入任何第三方框架时先排除它的日志包
# 2.修改application.yml配置项,也可以编写日志框架自己的配置文件logback-spring.xml/log4j2-spring.xml
# 3.如需对接专业日志系统只需要将logback记录的日志灌倒kafka之类的中间件,和springboot没关系,都是日志框架自己的配置
# 4.业务代码中使用slf4j-api记录日志代替System.out.println()
```

## 整合webmvc
```shell
# 场景启动器：spring-boot-starter-web - spring-boot-starter - spring-boot-autoconfigure
# 自动配置类：org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration - @Bean注册一堆SpringMVC组件
# 绑定属性类：org.springframework.boot.autoconfigure.web.{servlet.WebMvcProperties, WebProperties}
# 修改配置项：WebMvcAutoConfiguration - WebMvcProperties(prefix = "spring.mvc"), WebProperties("spring.web") - application.yml

# web开发三种模式
# 全自动：全部使用自动配置默认效果,直接进入Controller开发
# 全手动：全部禁用自动配置默认效果,@Configuration + WebMvcConfigurer,标注@EnableWebMvc
# 半自动：保留自动配置默认效果,手动配置部分功能定义Mvc底层组件,@Configuration + WebMvcConfigurer,不标注@EnableWebMvc

# 为什么容器中放一个WebMvcConfigurer就能配置底层行为
# WebMvcAutoConfiguration - EnableWebMvcConfiguration - DelegatingWebMvcConfiguration
# 源码45~53行通过DI将容器中所有的WebMvcConfigurer注入进来,包括我们自己写的MyWebMvcConfig,从而配置SpringMVC底层行为

# 为什么添加@EnableWebMvc就会禁用boot的默认配置
# @EnableWebMvc - 源码100行DelegatingWebMvcConfiguration - 继承自WebMvcConfigurationSupport - 
# 导致WebMvcAutoConfiguration源码146行的条件注解失效,那么该类下的所有SpringMVC默认配置也就无法生效,变成全手动开发模式

# 嵌入式容器
# 场景启动器：spring-boot-starter-web - spring-boot-starter-tomcat
# 自动配置类：org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
# 绑定属性类：org.springframework.boot.autoconfigure.web.ServerProperties
# 修改配置项：ServletWebServerFactoryAutoConfiguration - ServerProperties(prefix = "server") - application.yml

# ServletWebServerFactoryAutoConfiguration源码70行@Import导入了Tomcat、Jetty、Undertow三大嵌入式服务器,默认只有Tomcat的条件注解生效
# TomcatServletWebServerFactory - AbstractServletWebServerFactory - ConfigurableServletWebServerFactory - ServletWebServerFactory
# ServletWebServerFactory接口的getWebServer()专门用来生产web服务器,比如TomcatServletWebServerFactory源码201行创建了Tomcat

# 修改"server."开头的配置项就可以修改服务器参数,给容器中放一个ServletWebServerFactory,禁用SpringBoot默认服务器工厂,实现自定义服务器
# 如果想切换jetty服务器,先在spring-boot-starter-web将spring-boot-starter-tomcat排除,再单独引入spring-boot-starter-jetty
# 其实tomcat、jetty、netty这些服务器性能相差不大,高并发场景还得是响应式编程
```

## WebMvcAutoConfiguration源码分析
```shell
# 142~148行：条件注解

# 164行：注册HiddenHttpMethodFilter：页面表单提交Rest请求过滤器,GET、POST、PUT、DELETE
# 171行：注册FormContentFilter：表单内容过滤器,GET数据放url后面、POST数据放请求体、PUT和DELETE请求体数据会被忽略

# 183行：实现WebMvcConfigurer接口,该接口提供了配置SpringMVC底层所有组件的入口
# @EnableConfigurationProperties({ WebMvcProperties.class, WebProperties.class })
# public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer {...}
WebMvcConfigurer源码分析
# 源码58行：configurePathMatch 路径匹配
# 源码64行：configureContentNegotiation 内容协商
# 源码70行：configureAsyncSupport 异步支持
# 源码79行：configureDefaultServletHandling 默认Servlet,可以覆盖Tomcat的DefaultServlet
# 源码86行：addFormatters 格式化器,支持在属性上添加@NumberFormat和@DatetimeFormat进行数据类型转换
# 源码95行：addInterceptors 拦截器,拦截收到的所有请求
# 源码104行：addResourceHandlers 静态资源处理
# 源码120行：addCorsMappings 跨域
# 源码131行：addViewControllers 视图控制器
# 源码140行：configureViewResolvers 视图解析器,逻辑视图转为物理视图
# 源码150行：addArgumentResolvers 参数解析器
# 源码160行：addReturnValueHandlers 返回值解析器
# 源码179行：configureMessageConverters 消息转换器,标注@ResponseBody的返回值会利用MessageConverter直接写出去
# 源码210行：configureHandlerExceptionResolvers 异常处理器
# 源码231行：getValidator 数据校验,校验Controller上使用@Valid标注的参数是否合法,需要导入starter-validator 
# 源码241行：getMessageCodesResolver 消息码解析器,国际化使用

# 323行：默认静态资源规则
# a.访问"/webjars/**"就找"classpath:/META-INF/resources/webjars/"(不常用)
# b.访问"/**"就找"classpath:/META-INF/resources/","classpath:/resources/", "classpath:/static/", "classpath:/public/"

# 343行：静态资源都有缓存
# a.cachePeriod：缓存周期(秒)默认是0,所有缓存的设置都以"spring.web"开头
# b.cacheControl：HTTP缓存控制,官方文档 https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Caching
# c.useLastModified：是否使用最后一次修改,和服务器比对最后一次修改时间,如果没变就直接使用本地缓存,节省服务器资源

# 431行：WelcomePageHandlerMapping 欢迎页,项目启动时默认访问静态资源index.html,同时顺带访问favicon.ico,浏览器页面图标会发生变化

# 666行：ProblemDetailsExceptionHandler,条件注解生效需添加配置项spring.mvc.problemdetails.enabled=true
# 该类标注了@ControllerAdvice集中处理系统异常,并且继承自ResponseEntityExceptionHandler,点击去查看源码118行
# 当出现这些异常时会被SpringBoot以RFC7807规范方式返回错误数据,详见HelloController的method方法
```

## 整合mybatis
```shell
# 场景启动器：starter-xxx - spring-boot-starter - spring-boot-autoconfigure
# 自动配置类：org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration - @Import导入一堆数据源,等待生效
# 场景启动器：mybatis-spring-boot-starter - spring-boot-starter-jdbc - com.zaxxer.hikari.HikariDataSource - Hikari数据源生效
# 场景启动器：druid-spring-boot-3-starter - com.alibaba.druid.pool.DruidDataSource - Druid数据源生效
# 绑定属性类：org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
# 修改配置项：DataSourceAutoConfiguration - DataSourceProperties(prefix = "spring.datasource") - application.yml

# 场景启动器：mybatis-spring-boot-starter - mybatis-spring-boot-autoconfigure
# 自动配置类：org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration - @Bean注册组件SqlSessionFactory & SqlSessionTemplate
# 绑定属性类：org.mybatis.spring.boot.autoconfigure.MybatisProperties
# 修改配置项：MybatisAutoConfiguration - MybatisProperties(prefix = "mybatis") - application.yml
# 详见 UserController

# 如何分析导入场景启动器后会开启哪些自动配置类？
# spring-boot-autoconfigure、mybatis-spring-boot-autoconfigure、druid-spring-boot-3-starter
# classpath:/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
# 也可以在application.yml开启debug模式快速定位生效配置
```

## 整合redis
```shell
# 场景启动器：spring-boot-starter-data-redis - io.lettuce.lettuce-core
# 自动配置类：org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration - @Bean注册组件RedisTemplate
# 绑定属性类：org.springframework.boot.autoconfigure.data.redis.RedisProperties
# 修改配置项：RedisAutoConfiguration - RedisProperties(prefix = "spring.data.redis") - application.yml
# 定制化组件：RedisAutoConfiguration使用@Bean往容器放了些组件,在业务代码中自动注入即可,要是觉得不好用也可以自定义组件
# 详见 RedisController & RedisConfig
```

## 整合swagger
```shell
# Swagger可以快速生成实时接口,并且遵循OpenAPI规范,是前后端联调的Api文档生成工具
# 场景启动器：knife4j-openapi3-jakarta-spring-boot-starter (Knife4j是Swagger的增强版)
# 自动配置类：com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration
# 绑定属性类：com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties
# 修改配置项：Knife4jAutoConfiguration - Knife4jProperties(prefix = "knife4j") - application.yml
# 接口文档：http://localhost:8080/doc.html

# 常用注解：@Schema标注实体类/@Tag标注Controller类/@Operation标注方法/@Parameter标注参数/@ApiResponse标注响应状态码
# 分组管理：项目中的所有XxxController都放在一个页面有点臃肿,可以自定义配置类使用@Bean进行分组管理方便切换页面
# 详见 User & UserController & Knife4jConfig
```

## 整合kafka
```shell
# 场景启动器：spring-kafka
# 自动配置类：org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration - @Bean注册组件KafkaTemplate
# 绑定属性类：org.springframework.boot.autoconfigure.kafka.KafkaProperties
# 修改配置项：KafkaAutoConfiguration - KafkaProperties(prefix = "spring.kafka") - application.yml
# 详见 KafkaController
```

## 基础特性
```shell
# 1.banner在线生成工具 https://www.bootschool.net/ascii

# 2.环境切换
# 标识环境：可以给任意组件添加@Profile注解标识在哪个环境下生效,不添加该注解默认在所有环境都生效
# 切换环境：配置文件激活 spring.profiles.active=dev 命令行激活 java -jar xxx.jar --spring.profiles.active=dev
# application.properties 主配置文件,任何情况下都生效,优先级高于application.yaml
# application-${profile}.properties 在主配置文件激活才会生效,优先级高于主配置文件,配置项冲突时以激活环境为准

# 3.外部化配置
# 线上应用快速修改配置：在jar包外面放一个application.properties,重启项目就会自动应用最新配置
# springboot允许使用各种外部配置源,让同一套代码能适配多个不同环境,包括.properties文件/.yml文件/命令行参数
# 配置优先级：命令行 > (包外)激活配置文件 > (包外)主配置文件 > (包内)激活配置文件 > (包内)主配置文件
# 命令行参数会被添加到环境变量,属于最高优先级,java -jar xxx.jar --name="aaa" 可通过@Value("${name}")获取
# 包内：application.yml        server.port=8001
# 包内：application-dev.yml    server.port=8002
# 包外：application.yml        server.port=8003
# 包外：application-dev.yml    server.port=8004
# 启动端口：命令行 > 8004 > 8003 > 8002 > 8001
```

## 自定义starter
```shell
# 项目中会有一些独立于业务之外的通用功能,可以将其单独封装成starter,供其它项目引用
# 举例：将发短信功能单独抽取出来,其它项目导入此starter都具备发短信功能
# 1.创建自定义项目robot-starter,导入spring-boot-starter-xxx基础依赖和其它相关依赖
# 2.编写通用功能 - 选中项目 - 右键 - Run Maven - clean install 打包并部署到本地仓库
# 3.SpringBoot默认只扫描主程序所在包,第三方启动器的包路径是扫不到的,需要自定义RobotAutoConfiguration配置类
# 通过@Import或@Bean手动导入该启动器下的所有组件,然后在项目启动类添加@Import(RobotAutoConfiguration.class),这样看上去有点冗余
# 4.继续优化,借鉴SpringBoot的@EnableXxx,自定义@EnableRobot注解通过@Import(RobotAutoConfiguration.class)引入配置类,然后在项目启动类添加@EnableRobot
# 5.终极方案,借鉴SpringBoot的SPI机制,自定义META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
# 指定项目启动时要加载的所有配置类,这样就无需在项目启动类添加任何注解,实现完全自动化
```

## 整合actuator
```shell
# Spring Actuator可以更好地管理和监控SpringBoot应用 http://localhost:8080/actuator
# 场景启动器：spring-boot-starter-actuator - spring-boot-actuator-autoconfigure
# 自动配置类：org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration - @Bean注册一堆Endpoint组件
# 绑定属性类：org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties
# 修改配置项：WebEndpointAutoConfiguration - WebEndpointProperties(prefix = "management.endpoints.web") - application.yml

# 安装Prometheus时序数据库
docker run -p 9090:9090 -d -v pc:/etc/prometheus prom/prometheus
# 安装Grafana,默认账号密码admin/admin
docker run -d --name=grafana -p 3000:3000 grafana/grafana
```