package com.liruilong.activiti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.liruilong","org.activiti"})
public class DemoApplication
{
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
