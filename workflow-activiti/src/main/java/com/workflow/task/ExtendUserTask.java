package com.workflow.task;

import org.activiti.bpmn.model.UserTask;

import java.util.Map;

public class ExtendUserTask extends UserTask {

    protected Map<String,Object> user;

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }
}
