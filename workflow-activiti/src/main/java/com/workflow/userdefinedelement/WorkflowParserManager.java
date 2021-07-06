package com.workflow.userdefinedelement;

import com.workflow.userdefinedelement.parser.UserDefinedElementParser;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.util.io.InputStreamSource;

import java.io.InputStream;
import java.nio.charset.Charset;

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
}
