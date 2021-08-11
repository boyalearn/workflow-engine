package com.workflow.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.repository.DeploymentBuilderImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

public class ListenerTest {
    public static void main(String[] args) {
        StandaloneProcessEngineConfiguration configuration = (StandaloneProcessEngineConfiguration) ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&serverTimezone=UTC&characterEncoding=utf-8");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("12345678");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        ProcessEngine processEngine = configuration.buildProcessEngine();
        DeploymentBuilderImpl deployment = (DeploymentBuilderImpl) processEngine.getRepositoryService().createDeployment();
        deployment.disableBpmnValidation().disableSchemaValidation().name("listener").addClasspathResource("./diagrams/listener-node.bpmn").deploy();
        ProcessInstance processInstance = processEngine.getRuntimeService()
                //使用流程定义的key启动实例，key对应bpmn文件中id的属性值，默认按照最新版本流程启动
                .startProcessInstanceByKey("listener-process");

        System.out.println(processInstance);

        List<Task> list = configuration.buildProcessEngine().getTaskService().createTaskQuery().processDefinitionKey("listener-process").list();

        /*List<ProcessDefinition> list1 = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
        for (ProcessDefinition model : list1) {
            System.out.println(model);
        }*/

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
            processEngine.getTaskService().complete(task.getId());
            System.out.println();
        }
    }
}
