package com.liruilong.activiti.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;

/**
 * @Classname WebMvcConfig
 * @Description TODO
 * @Date 2021/6/16 3:09
 * @Created Li Ruilong
 */

@Configuration
public class WebMvcConfig {

    @Bean
    MappingJackson2HttpMessageConverter mappingJackson2CborHttpMessageConverter(){
        return new MappingJackson2HttpMessageConverter(objectMapper());
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return om;
    }
}
