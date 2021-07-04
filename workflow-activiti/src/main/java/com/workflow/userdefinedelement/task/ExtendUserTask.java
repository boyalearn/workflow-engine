package com.workflow.userdefinedelement.task;

import org.activiti.bpmn.model.UserTask;

public class ExtendUserTask extends UserTask {

    public ExtendUserTask(UserTask baseElement) {
        this.id = baseElement.getId();
        this.name = baseElement.getName();
        this.assignee = baseElement.getAssignee();
        this.owner = baseElement.getOwner();
        this.priority = baseElement.getPriority();
        this.formKey = baseElement.getFormKey();
        this.dueDate = baseElement.getDueDate();
        this.businessCalendarName = baseElement.getBusinessCalendarName();
        this.category = baseElement.getCategory();
        this.extensionId = baseElement.getExtensionId();
        this.candidateUsers = baseElement.getCandidateUsers();
        this.candidateGroups = baseElement.getCandidateGroups();
        this.formProperties = baseElement.getFormProperties();
        this.taskListeners = baseElement.getTaskListeners();
        this.skipExpression = baseElement.getSkipExpression();
        this.customUserIdentityLinks = baseElement.getCustomUserIdentityLinks();
        this.customGroupIdentityLinks = baseElement.getCustomGroupIdentityLinks();
        this.customProperties = baseElement.getCustomProperties();
    }


}
