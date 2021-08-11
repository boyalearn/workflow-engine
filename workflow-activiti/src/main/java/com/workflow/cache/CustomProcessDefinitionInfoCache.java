package com.workflow.cache;

import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionInfoCache;

public class CustomProcessDefinitionInfoCache extends ProcessDefinitionInfoCache {
    public CustomProcessDefinitionInfoCache(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
}
