package com.okccc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: okccc
 * @Date: 2024/1/26 17:27:44
 * @Desc: springboot整合了JUnit5,导入启动器spring-boot-starter-test - junit-jupiter
 */
// 导入该注解就可以测试SpringBoot应用容器中的所有组件
// 测试类也要放入主程序所在包,这样才能被扫描并加载到IOC容器
@SpringBootTest
public class ApplicationTest {

    @BeforeEach
    public void init() {
        System.out.println("========== begin ==========");
    }

    @AfterEach
    public void close() {
        System.out.println("========== end ==========");
    }

    @ParameterizedTest  // 参数化测试
    @ValueSource(strings = {"one", "two", "three"})
    @DisplayName("param test")
    public void parameterizedTest(String string) {
        System.out.println(string);
        Assertions.assertTrue(StringUtils.isNotBlank(string));
    }
}
