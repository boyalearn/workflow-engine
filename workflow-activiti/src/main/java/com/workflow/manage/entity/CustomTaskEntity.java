package com.workflow.manage.entity;

import lombok.Getter;
import lombok.Setter;
import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

@Getter
@Setter
public class CustomTaskEntity extends TaskEntity {
    private String remark;
}
