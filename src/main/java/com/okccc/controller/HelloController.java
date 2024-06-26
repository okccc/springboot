package com.okccc.controller;

import com.okccc.bean.Person;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

/**
 * @Author: okccc
 * @Date: 2023/12/30 17:03:45
 * @Desc: web开发详解
 */
@Slf4j
@RestController  // 点进去发现就等于 @Controller + @ResponseBody
public class HelloController {

    // http://localhost:8080/log?name=aaa&age=19
    @GetMapping(value = "log")
    public String log(String name, int age) {
        for (int i = 0; i < 10000; i++) {
            log.info("name = " + name + ", age = " + age);
        }
        return "hello springboot3!";
    }

    /**
     * 路径匹配：默认使用新版策略PATH_PATTERN_PARSER,比老版策略ANT_PATH_PARSER性能更好
     * 注意事项：新版策略**只能出现在末尾不能出现在中间,其它语法和老版策略相同
     * Ant风格路径写法：*任意数量的字符、?任意单个字符、**任意数量的目录、{}路径占位符、[]字符集合,[a-z]表示小写字母
     */
    // http://localhost:8080/a123/b1/1/2/3/abcdefg/abc/123
    @GetMapping(value = "/a*/b?/**/{p1:[a-z]+}/**")
    public String path(HttpServletRequest request, @PathVariable("p1") String path) {
        log.info("path variable p1: {}", path);
        return request.getRequestURI();
    }

    /**
     * 内容协商：一套系统适配多端请求动态返回不同格式数据
     * WebMvcAutoConfiguration - EnableWebMvcConfiguration - DelegatingWebMvcConfiguration - WebMvcConfigurationSupport
     * - addDefaultHttpMessageConverters 源码877行提供了几种默认的HttpMessageConverter,也可以自定义消息转换器增加新的内容协商功能
     * ByteArrayHttpMessageConverter：支持字节数据读写
     * StringHttpMessageConverter：支持字符串读写
     * ResourceHttpMessageConverter：支持资源读写
     * ResourceRegionHttpMessageConverter：支持分区资源写出
     * AllEncompassingFormHttpMessageConverter：支持表单xml/json读写
     * MappingJackson2HttpMessageConverter：支持请求响应体json读写
     *
     * 基于请求头实现(默认开启)
     * 客户端发送请求时携带Accept请求头,application/json、text/xml、text/yaml,可通过postman测试
     *
     * 基于请求参数实现(application.yml手动开启)
     * 客户端发送请求时携带请求参数?format=json或者?format=xml
     *
     * spring-boot-starter-web - spring-boot-starter-json - jackson-databind - jackson-core
     * web场景启动器引入了jackson处理json的包,所以默认将对象写成json
     * jackson也支持把数据写成xml,导入jackson-dataformat-xml依赖,并在实体类添加@JacksonXmlRootElement注解
     *
     * 如何增加支持yaml格式？
     * 配置媒体类型支持 spring.mvc.contentnegotiation.media-types.yaml=text/yaml
     * 自定义MyYamlHttpMessageConverter,告诉springboot这个消息转换器支持的媒体类型,然后将组件注册到容器
     */
    @Autowired
    private Person person;

    // http://localhost:8080/person
    // http://localhost:8080/person?format=json
    // http://localhost:8080/person?format=xml
    // http://localhost:8080/person?format=yaml
    @GetMapping(value = "person")
    public Person person() {
        return person;
    }

    /**
     * 国际化
     * 场景启动器：spring-boot-starter-web - spring-boot-starter - spring-boot-autoconfigure
     * 自动配置类：org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration
     * 绑定属性类：org.springframework.boot.autoconfigure.context.MessageSourceProperties
     * 修改配置项：MessageSourceAutoConfiguration - MessageSourceProperties(prefix = "spring.messages") - application.yml
     * 在resources目录下创建messages_en_US.properties和messages_zh_CN.properties文件
     * idea的Resource Bundle Editor插件会自动创建Resources Bundle 'messages'目录
     * 不同语言环境的页面展示效果,可通过设置浏览器的首选语言进行调试 chrome - settings - language
     */
    @Autowired
    private MessageSource messageSource;

    // http://localhost:8080/message
    @GetMapping(value = "message")
    public String message(HttpServletRequest request) {
        // 获取客户端的语言环境
        Locale locale = request.getLocale();
        System.out.println("locale = " + locale);  // locale = zh_CN
        // 获取Resource Bundle 'messages'目录下符合当前语言环境的配置文件中的国际化配置项
        return messageSource.getMessage("login", null, locale);
    }

    /**
     * thymeleaf
     * 场景启动器：spring-boot-starter-web - spring-boot-starter - spring-boot-autoconfigure
     * 自动配置类：org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
     * 绑定属性类：org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties
     * 修改配置项：ThymeleafAutoConfiguration - ThymeleafProperties(prefix = "spring.thymeleaf") - application.yml
     *
     * 前后端分离模式：@RestController直接响应JSON数据
     * 服务端页面渲染：@Controller + Thymeleaf模板引擎
     */
    // http://localhost:8080/view
    @GetMapping(value = "view")
    public String view(@RequestParam("name") String name, Model model) {
        // 把需要给页面共享的数据放到Model中
        String text = "<span style='color:red'>" + name + "</span>";
        model.addAttribute("msg", text);

        // 动态传入路径、样式、条件判断
        model.addAttribute("imgUrl", "/2.jpg");
        model.addAttribute("style", "width: 400px");
        model.addAttribute("show", true);

        // 物理视图 = 前缀 + 逻辑视图 + 后缀
        // 真实地址 = classpath:/templates/hello.html
        return "hello";
    }

    /**
     * 异常处理
     * 场景启动器：spring-boot-starter-web - spring-boot-starter - spring-boot-autoconfigure
     * 自动配置类：org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
     * 绑定属性类：org.springframework.boot.autoconfigure.web.ServerProperties
     * 修改配置项：ErrorMvcAutoConfiguration - ServerProperties(prefix = "server") - application.yml
     *
     * SpringMVC异常处理：@ExceptionHandler标识方法只处理当前类的错误,@ControllerAdvice标识类统一处理所有错误
     * 异常先由SpringMVC处理,不行再交给SpringBoot扩展的异常处理机制/error,浏览器响应白页,移动端(postman)响应json
     *
     * 源码详解
     * ErrorMvcAutoConfiguration - BasicErrorController源码85行返回HTML,95行返回ResponseEntity(JSON)
     *
     * 85行的errorHtml方法：
     * HttpStatus status = getStatus(request);
     * ModelAndView modelAndView = resolveErrorView(request, response, status, model);
     * return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
     * 先根据状态码解析错误视图 - 解析不到就去找error视图 - 找不到就用ErrorMvcAutoConfiguration源码151行
     * SpringBoot提供的默认error视图,源码198行render方法渲染部分就是浏览器经典的"Whitelabel Error Page"
     *
     * 其中resolveErrorView方法点进去进入DefaultErrorViewResolver源码101、109、119行
     * 能匹配精确码(404、500)：
     *   有模板引擎,就去找classpath:/templates/error/精确码.html
     *   没有模板引擎,就去找四大静态资源文件夹/精确码.html
     * 不能匹配精确码就去匹配模糊码(4xx、5xx)：
     *   有模板引擎,就去找classpath:/templates/error/模糊码.html
     *   没有模板引擎,就去找四大静态资源文件夹/模糊码.html
     *
     * 最佳实战
     * 前后端分离模式：@ControllerAdvice + @ExceptionHandler 统一处理后端所有错误
     * 服务端页面渲染：
     * 客户端或服务器错误：在classpath:/templates/error/目录下放精确码(404、500)页面和模糊码(4xx、5xx)页面
     * 通用业务错误：在classpath:/templates/error.html页面显示错误信息
     * 核心业务错误：每个错误都应该由代码控制,跳转到定制的错误页面
     */
    // http://localhost:8080/err
    @GetMapping(value = "err")
    public int err() {
        return 10/0;
    }

}
