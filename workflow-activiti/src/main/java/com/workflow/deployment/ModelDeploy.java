package com.workflow.deployment;

import com.workflow.config.ProcessEngineDatabaseConfiguration;
import com.workflow.userdefinedelement.WorkflowParserManager;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.repository.DeploymentBuilder;

public class ModelDeploy {
    public static void main(String[] args) {
        BpmnModel model = WorkflowParserManager.parser();
        DeploymentBuilder deploymentBuilder = ProcessEngineDatabaseConfiguration.getConfiguration().buildProcessEngine().getRepositoryService().createDeployment();
        deploymentBuilder.addBpmnModel("test", model);
    }
}
