package com.workflow.userdefinedelement;

import com.workflow.manage.CustomProcessEngineConfiguration;
import com.workflow.userdefinedelement.handler.ExtendsUserTaskParseHandler;
import com.workflow.userdefinedelement.parser.UserDefinedElementParser;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.repository.DeploymentBuilderImpl;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.parse.BpmnParseHandler;
import org.activiti.engine.runtime.ProcessInstance;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class WorkflowParserManager {

    public static BpmnModel parser() {
        InputStream bpmnFilePath = UserDefinedElementParser.class.getClassLoader().getResourceAsStream("diagrams/user-defined-element-parser.bpmn");
        InputStreamSource inputStreamSource = new InputStreamSource(bpmnFilePath);
        BpmnXMLConverter.addConverter(new UserDefinedElementParser());
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamSource, false, true, String.valueOf(Charset.defaultCharset()));
        System.out.println(bpmnModel);
        return bpmnModel;
    }

    public static String convertModelToXml(BpmnModel model) {
        BpmnXMLConverter converter = new BpmnXMLConverter();
        byte[] bytes = converter.convertToXML(model, "UTF-8");
        System.out.println(new String(bytes));
        return new String(bytes);
    }

    public static void main(String[] args) {
        StandaloneProcessEngineConfiguration configuration = new CustomProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti?useUnicode=true&serverTimezone=UTC&characterEncoding=utf-8");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("12345678");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        List<BpmnParseHandler> handlers = new ArrayList<>(2);
        handlers.add(new ExtendsUserTaskParseHandler());
        configuration.setCustomDefaultBpmnParseHandlers(handlers);
        //BpmnXMLConverter.addConverter(new UserDefinedElementParser());
        ProcessEngine processEngine = configuration.buildProcessEngine();
        DeploymentBuilderImpl deployment = (DeploymentBuilderImpl) processEngine.getRepositoryService().createDeployment();
        deployment.disableBpmnValidation().disableSchemaValidation().name("user-defined").addClasspathResource("./diagrams/user-defined-element-parser.bpmn").deploy();
        ProcessInstance processInstance = processEngine.getRuntimeService()
                //使用流程定义的key启动实例，key对应bpmn文件中id的属性值，默认按照最新版本流程启动
                .startProcessInstanceByKey("user-defined-element-process");

        System.out.println(processInstance);

    }
}
