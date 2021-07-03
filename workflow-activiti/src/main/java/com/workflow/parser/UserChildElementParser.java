package com.workflow.parser;

import com.workflow.task.LikeElement;
import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import javax.xml.stream.XMLStreamReader;

public class UserChildElementParser extends BaseChildElementParser {
    @Override
    public String getElementName() {
        return "like";
    }

    @Override
    public void parseChildElement(XMLStreamReader xmlStreamReader, BaseElement baseElement, BpmnModel bpmnModel) throws Exception {
        LikeElement extensionElement = new LikeElement();
        extensionElement.setName("like");
        baseElement.addExtensionElement(extensionElement);
        String value = xmlStreamReader.getAttributeValue("", "value");
        extensionElement.setValue(value);
    }
}
