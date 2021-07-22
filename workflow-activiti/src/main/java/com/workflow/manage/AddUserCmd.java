package com.workflow.manage;

import com.workflow.manage.entity.CustomUserEntity;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class AddUserCmd implements Command<Void> {
    @Override
    public Void execute(CommandContext commandContext) {
        CustomUserEntity userEntity = new CustomUserEntity();
        userEntity.setId("32323231");
        userEntity.setAge(13);
        commandContext.getDbSqlSession().insert(userEntity);
        return null;
    }

    public static void main(String[] args) {
        StandaloneProcessEngineConfiguration configuration = new CustomProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&serverTimezone=UTC&characterEncoding=utf-8");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("12345678");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        processEngine.getManagementService().executeCommand(new AddUserCmd());
    }
}
