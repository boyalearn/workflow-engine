package com.workflow.userdefinedelement.parser;

import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class UserElementParser extends BaseBpmnXMLConverter {


    @Override
    protected Class<? extends BaseElement> getBpmnElementType() {
        return null;
    }

    @Override
    protected BaseElement convertXMLToElement(XMLStreamReader xmlStreamReader, BpmnModel bpmnModel) throws Exception {
        return null;
    }

    @Override
    protected String getXMLElementName() {
        return null;
    }

    @Override
    protected void writeAdditionalAttributes(BaseElement baseElement, BpmnModel bpmnModel, XMLStreamWriter xmlStreamWriter) throws Exception {

    }

    @Override
    protected void writeAdditionalChildElements(BaseElement baseElement, BpmnModel bpmnModel, XMLStreamWriter xmlStreamWriter) throws Exception {

    }
}
