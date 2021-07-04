package com.workflow.userdefinedelement.task;

import org.activiti.bpmn.model.ExtensionElement;

public class LikeElement extends ExtensionElement {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
