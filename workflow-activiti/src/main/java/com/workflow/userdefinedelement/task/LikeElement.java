package com.workflow.userdefinedelement.task;

import lombok.Getter;
import lombok.Setter;
import org.activiti.bpmn.model.ExtensionElement;

@Getter
@Setter
public class LikeElement extends ExtensionElement {

    private String value;

}
