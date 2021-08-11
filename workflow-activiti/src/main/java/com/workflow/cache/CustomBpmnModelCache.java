package com.workflow.cache;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;

public class CustomBpmnModelCache implements DeploymentCache<BpmnModel> {
    @Override
    public BpmnModel get(String id) {
        return null;
    }

    @Override
    public void add(String id, BpmnModel object) {

    }

    @Override
    public void remove(String id) {

    }

    @Override
    public void clear() {

    }
}
