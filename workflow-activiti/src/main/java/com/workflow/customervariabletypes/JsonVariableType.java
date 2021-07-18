package com.workflow.customervariabletypes;

import com.workflow.idgenerator.CustomIdGenerator;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.variable.ValueFields;
import org.activiti.engine.impl.variable.VariableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonVariableType implements VariableType {
    @Override
    public String getTypeName() {
        return "json";
    }

    @Override
    public boolean isCachable() {
        return true;
    }

    @Override
    public boolean isAbleToStore(Object o) {
        if (null == o) {
            return true;
        }
        return Map.class.isAssignableFrom(o.getClass());
    }

    @Override
    public void setValue(Object o, ValueFields valueFields) {
        valueFields.setTextValue(o.toString());
    }

    @Override
    public Object getValue(ValueFields valueFields) {
        return new HashMap();
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
        List list = new ArrayList();
        list.add(new JsonVariableType());
        configuration.setCustomPreVariableTypes(list);
    }
}
