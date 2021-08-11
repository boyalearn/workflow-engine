package com.workflow.cache;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

import java.util.HashMap;
import java.util.Map;

public class CustomProcessDefinitionCache implements DeploymentCache<ProcessDefinitionEntity> {

    private Map<String,ProcessDefinitionEntity> cache=new HashMap<>();

    @Override
    public ProcessDefinitionEntity get(String id) {
        return cache.get(id);
    }

    @Override
    public void add(String id, ProcessDefinitionEntity object) {
        cache.put(id,object);
    }

    @Override
    public void remove(String id) {
        cache.remove(id);

    }

    @Override
    public void clear() {
        cache.clear();
    }

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
    }
}
