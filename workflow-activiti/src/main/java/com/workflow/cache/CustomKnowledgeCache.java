package com.workflow.cache;

import org.activiti.engine.impl.persistence.deploy.DeploymentCache;

public class CustomKnowledgeCache implements DeploymentCache<Object> {
    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public void add(String id, Object object) {

    }

    @Override
    public void remove(String id) {

    }

    @Override
    public void clear() {

    }
}
