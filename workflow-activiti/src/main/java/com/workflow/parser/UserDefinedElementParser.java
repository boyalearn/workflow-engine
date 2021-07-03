package com.workflow.parser;

import com.workflow.task.ExtendUserTask;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.UserTaskXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.activiti.bpmn.model.alfresco.AlfrescoUserTask;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDefinedElementParser extends UserTaskXMLConverter {
    public UserDefinedElementParser() {
        super();
        UserChildElementParser userChildElementParser = new UserChildElementParser();
        super.childParserMap.put(userChildElementParser.getElementName(), userChildElementParser);
    }

    @Override
    public Class<? extends BaseElement> getBpmnElementType() {
        return super.getBpmnElementType();
    }

    @Override
    protected String getXMLElementName() {
        return super.getXMLElementName();
    }

    @Override
    protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
        String formKey = xtr.getAttributeValue("http://activiti.org/bpmn", "formKey");
        UserTask userTask = null;
        if (StringUtils.isNotEmpty(formKey) && model.getUserTaskFormTypes() != null && model.getUserTaskFormTypes().contains(formKey)) {
            userTask = new AlfrescoUserTask();
        }

        if (userTask == null) {
            userTask = new ExtendUserTask();
        }

        BpmnXMLUtil.addXMLLocation((BaseElement) userTask, xtr);
        ((UserTask) userTask).setDueDate(xtr.getAttributeValue("http://activiti.org/bpmn", "dueDate"));
        ((UserTask) userTask).setCategory(xtr.getAttributeValue("http://activiti.org/bpmn", "category"));
        ((UserTask) userTask).setFormKey(formKey);
        ((UserTask) userTask).setAssignee(xtr.getAttributeValue("http://activiti.org/bpmn", "assignee"));
        ((UserTask) userTask).setOwner(xtr.getAttributeValue("http://activiti.org/bpmn", "owner"));
        ((UserTask) userTask).setPriority(xtr.getAttributeValue("http://activiti.org/bpmn", "priority"));
        Map<String, Object> user = new HashMap<String, Object>();
        String attributeValue = xtr.getAttributeValue("http://www.user.com/user", "name");
        user.put("name", attributeValue);
        ((ExtendUserTask) userTask).setUser(user);
        String expression;
        if (StringUtils.isNotEmpty(xtr.getAttributeValue("http://activiti.org/bpmn", "candidateUsers"))) {
            expression = xtr.getAttributeValue("http://activiti.org/bpmn", "candidateUsers");
            ((UserTask) userTask).getCandidateUsers().addAll(this.parseDelimitedList(expression));
        }

        if (StringUtils.isNotEmpty(xtr.getAttributeValue("http://activiti.org/bpmn", "candidateGroups"))) {
            expression = xtr.getAttributeValue("http://activiti.org/bpmn", "candidateGroups");
            ((UserTask) userTask).getCandidateGroups().addAll(this.parseDelimitedList(expression));
        }

        if (StringUtils.isNotEmpty(xtr.getAttributeValue("http://activiti.org/bpmn", "skipExpression"))) {
            expression = xtr.getAttributeValue("http://activiti.org/bpmn", "skipExpression");
            ((UserTask) userTask).setSkipExpression(expression);
        }

        BpmnXMLUtil.addCustomAttributes(xtr, (BaseElement) userTask, new List[]{defaultElementAttributes, defaultActivityAttributes, defaultUserTaskAttributes});
        this.parseChildElements(this.getXMLElementName(), (BaseElement) userTask, this.childParserMap, model, xtr);
        return (BaseElement) userTask;
    }

    @Override
    protected void writeAdditionalAttributes(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {
        super.writeAdditionalAttributes(element, model, xtw);
    }

    @Override
    protected boolean writeExtensionChildElements(BaseElement element, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
        return super.writeExtensionChildElements(element, didWriteExtensionStartElement, xtw);
    }

    @Override
    protected boolean writeCustomIdentities(BaseElement element, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
        return super.writeCustomIdentities(element, didWriteExtensionStartElement, xtw);
    }

    @Override
    protected void writeCustomIdentities(UserTask userTask, String identityType, Set<String> users, Set<String> groups, XMLStreamWriter xtw) throws Exception {
        super.writeCustomIdentities(userTask, identityType, users, groups, xtw);
    }

    @Override
    protected void writeAdditionalChildElements(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {
        super.writeAdditionalChildElements(element, model, xtw);
    }

    public static void main(String[] args) {
        InputStream bpmnFilePath = UserDefinedElementParser.class.getClassLoader().getResourceAsStream("diagrams/user-defined-element-parser.bpmn");
        InputStreamSource inputStreamSource = new InputStreamSource(bpmnFilePath);
        BpmnXMLConverter.addConverter(new UserDefinedElementParser());
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamSource, false, true, String.valueOf(Charset.defaultCharset()));
        System.out.println(bpmnModel);
    }

}
