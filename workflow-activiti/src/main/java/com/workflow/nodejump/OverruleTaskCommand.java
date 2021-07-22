package com.workflow.nodejump;

import lombok.AllArgsConstructor;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.GatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.repository.DeploymentBuilderImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * 1.确认需要跳转的节点以及跳转的目标节点
 * 2.确认需要跳转节点归属的流程文档
 * 3.获取需要体跳转的流程实例与执行实例
 * 4.确认跳转到的目标节点之后是否需要添加变量
 * 5.确认跳转到的目标节点师傅需要触发目标节点配置监听器
 * 6.如何处理流程实例存在分支或者多实例的情况。
 */
public class OverruleTaskCommand implements Command<Void> {

    private String taskId;

    public OverruleTaskCommand(String taskId) {
        this.taskId = taskId;
    }

    private static final String OVERRULE_REASON = "overrule";


    @Override
    public Void execute(CommandContext commandContext) {
        //找到当前任务
        TaskEntity task = commandContext.getTaskEntityManager().findTaskById(this.taskId);

        //找到当前流程引擎中定于义的活动
        ProcessEngine processEngine = Context.getProcessEngineConfiguration().buildProcessEngine();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) processEngine.getRepositoryService().getProcessDefinition(task.getProcessDefinitionId());
        ActivityImpl activity = processDefinition.getActivities()
                .stream().filter(r -> r.getId().equals(task.getTaskDefinitionKey())).findFirst().get();
        //找到当前执行实例
        String executionId = task.getExecutionId();
        ExecutionEntityManager executionEntityManager = Context.getCommandContext().getExecutionEntityManager();
        ExecutionEntity execution = executionEntityManager.findExecutionById(executionId);
        execution.setEventSource(activity);
        execution.setActivity(activity);
        //触发事件
        task.fireEvent(TaskListener.EVENTNAME_COMPLETE);
        //删除兄弟任务
        List<ExecutionEntity> brothersExecutions = executionEntityManager.findChildExecutionsByParentExecutionId(execution.getParentId());
        for (ExecutionEntity executionEntity : brothersExecutions) {
            List<TaskEntity> tasks = Context.getCommandContext().getTaskEntityManager().findTasksByExecutionId(executionEntity.getId());
            for (TaskEntity delTask : tasks) {
                Context.getCommandContext().getTaskEntityManager().deleteTask(delTask, OVERRULE_REASON, false);
            }
        }
        //找到上一个活动
        List<PvmTransition> incomingTransitions = activity.getIncomingTransitions();
        for (PvmTransition incomingTransition : incomingTransitions) {
            ActivityImpl source = (ActivityImpl)incomingTransition.getSource();
            execution.executeActivity(getActivity(source));
        }
        return null;
    }

    private ActivityImpl getActivity(ActivityImpl source){
        if(!UserTaskActivityBehavior.class.isAssignableFrom(source.getActivityBehavior().getClass())){
            if(source.getIncomingTransitions().size()<=0){
                throw new RuntimeException("can not overrule");
            }
            return getActivity((ActivityImpl) source.getIncomingTransitions().get(0).getSource());
        }
        return source;
    }

    public static void main(String[] args) {
        StandaloneProcessEngineConfiguration configuration = (StandaloneProcessEngineConfiguration) ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&serverTimezone=UTC&characterEncoding=utf-8");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("12345678");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        /*deploy(configuration);*/
        List<Task> list = processEngine.getTaskService().createTaskQuery().list();
        for (Task task : list) {
            processEngine.getTaskService().complete(task.getId());
        }

        list = processEngine.getTaskService().createTaskQuery().list();
        try {
            TaskServiceImpl taskService = (TaskServiceImpl)processEngine.getTaskService();
            taskService.getCommandExecutor().execute(new OverruleTaskCommand(list.get(0).getId()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deploy(StandaloneProcessEngineConfiguration configuration){
        ProcessEngine processEngine = configuration.buildProcessEngine();

        //管理流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilderImpl deployment = (DeploymentBuilderImpl)repositoryService.createDeployment();
        deployment.disableBpmnValidation().disableSchemaValidation().name("jump-node-demo").addClasspathResource("./diagrams/node-jump.bpmn").deploy();
        ProcessInstance processInstance = processEngine.getRuntimeService()
                //使用流程定义的key启动实例，key对应bpmn文件中id的属性值，默认按照最新版本流程启动
                .startProcessInstanceByKey("node-jump");
    }
}
