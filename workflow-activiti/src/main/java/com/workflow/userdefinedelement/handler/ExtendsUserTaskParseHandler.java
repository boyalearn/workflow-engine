package com.workflow.userdefinedelement.handler;

import com.workflow.userdefinedelement.task.LikeElement;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendsUserTaskParseHandler extends UserTaskParseHandler {
    @Override
    protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
        super.executeParse(bpmnParse, userTask);
        ActivityImpl activity = bpmnParse.getCurrentScope().findActivity(userTask.getId());
        Map<String, Object> operationMap = parseUserTaskOperations(bpmnParse, userTask);

        //将扩展属性设置给activity
        activity.setProperty("ui", operationMap);
    }

    private Map<String,Object> parseUserTaskOperations(BpmnParse bpmnParse, UserTask userTask) {
        Map<String, Object> operationMap = new HashMap<String, Object>();
        //获取扩展属性标签元素
        List<ExtensionElement> operationsElement = userTask.getExtensionElements().get("ui");

        if (operationsElement != null) {
            for (ExtensionElement operationElement : operationsElement) {
                operationMap.put(operationElement.getName(),((LikeElement)operationElement).getValue());
            }
        }

        return operationMap;

    }
}
