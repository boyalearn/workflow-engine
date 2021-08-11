package com.workflow.userdefinedelement.parser;

import com.workflow.userdefinedelement.task.LikeElement;
import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import javax.xml.stream.XMLStreamReader;


/**
 * <user:like value="coding"/>
 *
 * 示例模板
 *
 * user-defined-element-parser
 *
 */
public class UserChildAttributeParser extends BaseChildElementParser {

    private static final String ELEMENT_NAME = "ui";

    private static final String VALUE_KEY = "value";

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public void parseChildElement(XMLStreamReader xmlStreamReader, BaseElement baseElement, BpmnModel bpmnModel) throws Exception {
        LikeElement extensionElement = new LikeElement();
        extensionElement.setName(ELEMENT_NAME);
        baseElement.addExtensionElement(extensionElement);
        String value = xmlStreamReader.getAttributeValue("", VALUE_KEY);
        extensionElement.setValue(value);
    }
}
