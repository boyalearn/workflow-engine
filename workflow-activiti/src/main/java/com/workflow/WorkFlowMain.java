package com.workflow;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

public class WorkFlowMain {

    public static void main(String[] args) {

        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&serverTimezone=UTC&characterEncoding=utf-8");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("");

        //不自动创建表，需要表存在 DB_SCHEMA_UPDATE_FALSE = "false";
        //先删除表，再创建表 DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
        //如果表不存在，先创建表 DB_SCHEMA_UPDATE_TRUE = "true";
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        //创建工作流核心对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println(processEngine);

        //管理流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //repositoryService.deleteDeployment("helloworld");
        /*Deployment deployment = repositoryService.createDeployment().name("请假审批").addClasspathResource("./diagrams/helloworld.bpmn").deploy();
        System.out.println(deployment);

        ProcessInstance processInstance = processEngine.getRuntimeService()
                //使用流程定义的key启动实例，key对应bpmn文件中id的属性值，默认按照最新版本流程启动
                .startProcessInstanceByKey("helloworld");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getProcessDefinitionId());*/
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()  //创建查询任务对象
                .list();

        for(Task task:list){
            System.out.println(task);
            processEngine.getTaskService().setVariable(task.getId(),"approve",false);
            processEngine.getTaskService().complete(task.getId());
        }
        //执行管理，包括启动、推进、删除流程实例等
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //任务管理
        TaskService taskService = processEngine.getTaskService();
        //历史管理（执行完的数据的管理
        HistoryService historyService = processEngine.getHistoryService();
        //组织机构管理
        IdentityService identityService = processEngine.getIdentityService();
        //可选服务，任务表单管理
        FormService formService = processEngine.getFormService();

        ManagementService managementService = processEngine.getManagementService();
    }
}
