package com.workflow.userdefinedelement.parser;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.UserTaskXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.util.io.InputStreamSource;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 增加一种子元素解析类型
 */
public class UserDefinedElementParser extends UserTaskXMLConverter {
    public UserDefinedElementParser() {
        super();
        UserChildAttributeParser userChildElementParser = new UserChildAttributeParser();
        super.childParserMap.put(userChildElementParser.getElementName(), userChildElementParser);
    }

    public static void main(String[] args) {
        InputStream bpmnFilePath = UserDefinedElementParser.class.getClassLoader().getResourceAsStream("diagrams/user-defined-element-parser.bpmn");
        InputStreamSource inputStreamSource = new InputStreamSource(bpmnFilePath);
        //BpmnXMLConverter.addConverter(new UserDefinedElementParser());
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamSource, false, true, String.valueOf(Charset.defaultCharset()));
        System.out.println(bpmnModel);
    }


}
