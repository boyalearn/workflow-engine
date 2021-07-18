package com.workflow.nodejump;

import lombok.AllArgsConstructor;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.List;

/**
 * 1.确认需要跳转的节点以及跳转的目标节点
 * 2.确认需要跳转节点归属的流程文档
 * 3.获取需要体跳转的流程实例与执行实例
 * 4.确认跳转到的目标节点之后是否需要添加变量
 * 5.确认跳转到的目标节点师傅需要触发目标节点配置监听器
 * 6.如何处理流程实例存在分支或者多实例的情况。
 */
@AllArgsConstructor
public class OverruleTaskCommand implements Command<Void> {

    private String taskId;

    private static final String OVERRULE_REASON = "overrule";


    @Override
    public Void execute(CommandContext commandContext) {
        TaskEntity task = commandContext.getTaskEntityManager().findTaskById(this.taskId);

        ProcessEngine processEngine = Context.getProcessEngineConfiguration().buildProcessEngine();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) processEngine.getRepositoryService().getProcessDefinition(task.getProcessDefinitionId());
        ActivityImpl activity = processDefinition.getActivities()
                .stream().filter(r -> r.getId().equals(task.getTaskDefinitionKey())).findFirst().get();

        String executionId = task.getExecutionId();
        ExecutionEntityManager executionEntityManager = Context.getCommandContext().getExecutionEntityManager();
        ExecutionEntity execution = executionEntityManager.findExecutionById(executionId);

        execution.setEventSource(activity);
        execution.setActivity(activity);

        task.fireEvent(TaskListener.EVENTNAME_COMPLETE);
        Context.getCommandContext().getTaskEntityManager().deleteTask(task, OVERRULE_REASON, false);

        //删除兄弟任务
        List<ExecutionEntity> brothersExecutions = executionEntityManager.findChildExecutionsByParentExecutionId(execution.getParentId());
        for (ExecutionEntity brothersExecution : brothersExecutions) {
            brothersExecution.remove();
        }
        //找到上一个活动
        List<PvmTransition> incomingTransitions = activity.getIncomingTransitions();
        for (PvmTransition incomingTransition : incomingTransitions) {
            execution.executeActivity(incomingTransition.getSource());
        }
        return null;
    }
}
