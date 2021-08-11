package com.workflow.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;

import java.util.List;

public class CustomTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String processInstanceId = delegateTask.getProcessInstanceId();
        System.out.println(processInstanceId);
        System.out.println(""+delegateTask);

        ProcessEngine processEngine = Context.getProcessEngineConfiguration().buildProcessEngine();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) processEngine.getRepositoryService().getProcessDefinition(delegateTask.getProcessDefinitionId());
        ActivityImpl activity = processDefinition.getActivities()
                .stream().filter(r -> r.getId().equals(delegateTask.getTaskDefinitionKey())).findFirst().get();
        System.out.println(activity);
        PvmTransition pvmTransition = activity.getOutgoingTransitions().get(0);
        ActivityImpl source = (ActivityImpl)pvmTransition.getDestination();

        List<Task> list = processEngine.getTaskService().createTaskQuery().processDefinitionKey("listener-process").processInstanceId(processInstanceId).list();
        System.out.println(list);
    }
}
