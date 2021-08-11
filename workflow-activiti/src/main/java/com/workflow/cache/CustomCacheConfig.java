package com.workflow.cache;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.repository.DeploymentBuilderImpl;
import org.activiti.engine.repository.DeploymentBuilder;

public class CustomCacheConfig {
    public static void main(String[] args) {
        StandaloneProcessEngineConfiguration configuration = (StandaloneProcessEngineConfiguration) ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&serverTimezone=UTC&characterEncoding=utf-8");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("");
        //不自动创建表，需要表存在 DB_SCHEMA_UPDATE_FALSE = "false";
        //先删除表，再创建表 DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
        //如果表不存在，先创建表 DB_SCHEMA_UPDATE_TRUE = "true";
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.setProcessDefinitionCache(new CustomProcessDefinitionCache());
        DeploymentManager deploymentManager = configuration.getDeploymentManager();
        deploymentManager.setBpmnModelCache(new CustomBpmnModelCache());
        deploymentManager.setProcessDefinitionInfoCache(new CustomProcessDefinitionInfoCache(configuration.getCommandExecutor()));
        deploymentManager.setKnowledgeBaseCache(new CustomKnowledgeCache());
        configuration.setEnableProcessDefinitionInfoCache(true);

    }
}
