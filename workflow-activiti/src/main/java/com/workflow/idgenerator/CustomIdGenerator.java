package com.workflow.idgenerator;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;

import java.util.UUID;

public class CustomIdGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        return "system1"+UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        StandaloneProcessEngineConfiguration configuration = (StandaloneProcessEngineConfiguration) ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&serverTimezone=UTC&characterEncoding=utf-8");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("12345678");
        //不自动创建表，需要表存在 DB_SCHEMA_UPDATE_FALSE = "false";
        //先删除表，再创建表 DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
        //如果表不存在，先创建表 DB_SCHEMA_UPDATE_TRUE = "true";
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.setIdGenerator(new CustomIdGenerator());
    }
}
