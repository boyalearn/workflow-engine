package com.workflow.manage;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.task.Task;

import java.io.InputStream;
import java.util.List;

/**
 * 1.接管mybatis的mapper文件
 * 2.修改对应的SQL
 * 3.修改对应的承载实体类
 */
public class CustomProcessEngineConfiguration extends StandaloneProcessEngineConfiguration {
    public CustomProcessEngineConfiguration() {
        setDbIdentityUsed(false);
    }

    @Override
    protected InputStream getMyBatisXmlConfigurationSteam() {
        return getResourceAsStream("mapping/mappings.xml");
    }

    public static void main(String[] args) {
        StandaloneProcessEngineConfiguration configuration = new CustomProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&serverTimezone=UTC&characterEncoding=utf-8");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("12345678");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        /*deploy(configuration);*/
        List<Task> list = processEngine.getTaskService().createTaskQuery().list();
        for (Task task : list) {
            processEngine.getTaskService().complete(task.getId());
        }
    }
}
