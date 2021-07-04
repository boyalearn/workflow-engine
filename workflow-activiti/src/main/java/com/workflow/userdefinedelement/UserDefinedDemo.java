package com.workflow.userdefinedelement;

import com.workflow.BaseApplication;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserDefinedDemo extends BaseApplication {

    private static ConcurrentHashMap<String, List<ActivityImpl>> processActivityMap = new ConcurrentHashMap();

    public static void main(String[] args) {
        ProcessEngineConfiguration config = config(args);

        deploy();
        List<Task> list = config.buildProcessEngine().getTaskService().createTaskQuery().list();
        ProcessEngine processEngine = config.buildProcessEngine();

        List<ProcessDefinition> list1 = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
        for (ProcessDefinition model : list1) {
            System.out.println(model);
        }

        for (Task task : list) {
            String processDefinitionId = task.getProcessDefinitionId();
            String processInstanceId = task.getProcessInstanceId();
            String id = task.getId();
            String definedKey = task.getTaskDefinitionKey();
            System.out.println("processDefinitionId:" + processDefinitionId);
            System.out.println("processInstanceId:" + processInstanceId);
            System.out.println("taskId:" + id);
            ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) processEngine.getRepositoryService().getProcessDefinition(processDefinitionId);
            TaskDefinition taskDefinition = processDefinition.getTaskDefinitions().get(definedKey);
            List<HistoricTaskInstance> historicTaskInstances = queryHistoryTask(processInstanceId);
            System.out.println();
        }

    }

    public static List<HistoricTaskInstance> queryHistoryTask(String historyProcessKey){
        List<HistoricTaskInstance> list = applicationConfiguration.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(historyProcessKey).orderByTaskCreateTime().asc().list();

        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println(historicTaskInstance);
        }
        return list;
    }

    public static void main(Task task) {
        String taskDefinitionKey = task.getTaskDefinitionKey();
        TaskQuery taskQuery = applicationConfiguration.getTaskService().createTaskQuery();
        ExecutionQuery executionQuery = applicationConfiguration.getRuntimeService().createExecutionQuery();

        if (task != null) {

            String executionId = task.getExecutionId();
            Execution execution = executionQuery.executionId(executionId).singleResult();

            String activityId = execution.getActivityId();

            List<ActivityImpl> allActivities = getAllActivities(task.getTaskDefinitionKey());

            for (ActivityImpl activity : allActivities) {

                String id = activity.getId();

                if (!id.equals(activityId)) {

                    continue;

                }
                List<PvmTransition> incomingTransitions = activity.getIncomingTransitions();

                for (PvmTransition transition : incomingTransitions) {

                    PvmActivity source = transition.getSource();

                    String preTaskId = source.getId();


                }

            }

        }

    }

    public static List<ActivityImpl> getAllActivities(String dkey) {

        List<ActivityImpl> activities = processActivityMap.get(dkey);

        if (activities == null) {
            RepositoryService repositoryService = applicationConfiguration.getRepositoryService();
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(dkey).singleResult();

            ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)

                    .getDeployedProcessDefinition(definition.getId());


            //获取所有的activity

            activities = processDefinition.getActivities();

            processActivityMap.put(dkey, activities);

        }

        return activities;
    }

}
