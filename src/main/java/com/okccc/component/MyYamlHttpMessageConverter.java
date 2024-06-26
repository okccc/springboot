package com.okccc.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author: okccc
 * @Date: 2024/1/13 17:13:44
 * @Desc: 自定义一个yaml格式的MessageConverter
 */
public class MyYamlHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    private final ObjectMapper objectMapper;

    public MyYamlHttpMessageConverter() {
        // 告诉springboot这个MessageConverter支持哪种媒体类型
        super(new MediaType("text", "yaml", StandardCharsets.UTF_8));
        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        this.objectMapper = new ObjectMapper(factory);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // 只要对象类型,不要基本类型
        return true;
    }

    @Override  // @RequestBody
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override  // @ResponseBody,把对象怎么写出去
    protected void writeInternal(Object methodReturnValue, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // try with写法,自动关闭流
        try(OutputStream os = outputMessage.getBody()) {
            objectMapper.writeValue(os, methodReturnValue);
        }
    }
}
