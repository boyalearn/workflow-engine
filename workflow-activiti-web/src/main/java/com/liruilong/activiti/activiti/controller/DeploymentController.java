package com.liruilong.activiti.activiti.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liruilong.activiti.activiti.vo.DeploymentResponse;
import com.liruilong.activiti.common.RestServiceController;
import com.liruilong.activiti.util.ToWeb;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 发布流程
 */
@RestController
@RequestMapping("deployments")
public class DeploymentController implements RestServiceController<Deployment, String> {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ProcessEngine processDefinitionId;


    /*
     * @return
     * @Description   xml下载
     * @author Liruilong
     * @date  2021/6/16  22:31
     **/
    @GetMapping("/outDeploymentsOpen/{id}")
    public ResponseEntity<byte[]> outDeploymentsOpen(@PathVariable("id") String id) throws IOException {
        BpmnModel bpmnModel = ModelId2BpmnModel(id);
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
        String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(bpmnBytes,headers, HttpStatus.CREATED);
    }

    /*
     * @return
     * @Description  xml预览
     * @author Liruilong
     * @date  2021/6/17  0:27
     **/
    @GetMapping("/previewDeploymentsOpen/{id}")
    public String previewDeploymentsOpen(@PathVariable("id") String id) throws IOException {
        BpmnModel bpmnModel = ModelId2BpmnModel(id);
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
        String s = new String(bpmnBytes, "utf-8");
        s=  "```xml\n"+s+"\n```";
        return s;
    }

    /*
     * @return
     * @Description  图片预览
     * @author Liruilong
     * @date  2021/6/16  22:32
     **/
    @GetMapping("/previewDeploymentsOpenView/{id}")
    public String previewDeploymentsOpenView(@PathVariable("id") String id) throws IOException {
        BpmnModel bpmnModel = ModelId2BpmnModel(id);
        InputStream imageStream = new DefaultProcessDiagramGenerator().generatePngDiagram(bpmnModel);
        return Base64.getEncoder().encodeToString(IOUtils.toByteArray(imageStream));
    }

    /*
     * @return
     * @Description  图片下载
     * @author Liruilong
     * @date  2021/6/17  0:30
     **/
    @GetMapping("/outDeploymentsOpenView/{id}")
    public ResponseEntity<byte[]> outDeploymentsOpenView(@PathVariable("id") String id) throws IOException {
        BpmnModel bpmnModel = ModelId2BpmnModel(id);
        InputStream imageStream = new DefaultProcessDiagramGenerator().generatePngDiagram(bpmnModel);
        String filename = bpmnModel.getMainProcess().getId() + ".png";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(IOUtils.toByteArray(imageStream),headers, HttpStatus.CREATED);
    }

    /*
     * @return
     * @Description  ModelId ---> BpmnModel
     * @author Liruilong
     * @date  2021/6/17  0:45
     **/
    public BpmnModel ModelId2BpmnModel(String ModelId) throws IOException {
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        JsonNode editorNode = objectMapper.readTree(repositoryService.getModelEditorSource(ModelId));
        return  jsonConverter.convertToBpmnModel(editorNode);
    }


    @Override
    public Object getOne(@PathVariable("id") String id) {
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(id).singleResult();
        return ToWeb.buildResult().setObjData(new DeploymentResponse(deployment));
    }

    @Override
    public Object getList(@RequestParam(value = "rowSize", defaultValue = "1000", required = false) Integer rowSize, @RequestParam(value = "page", defaultValue = "1", required = false) Integer page) {
        List<Deployment> deployments = repositoryService.createDeploymentQuery()
                .listPage(rowSize * (page - 1), rowSize);
        long count = repositoryService.createDeploymentQuery().count();
        List<DeploymentResponse> list = new ArrayList<>();
        for(Deployment deployment: deployments){
            list.add(new DeploymentResponse(deployment));
        }

        return ToWeb.buildResult().setRows(
                ToWeb.Rows.buildRows()
                        .setRowSize(rowSize)
                        .setTotalPages((int) (count/rowSize+1))
                        .setTotalRows(count)
                        .setList(list)
                        .setCurrent(page)
        );
    }

    @Override
    public Object deleteOne(@PathVariable("id") String id) {
        repositoryService.deleteDeployment(id);
        return ToWeb.buildResult().msg("删除成功！");
    }

    @Override
    public Object postOne(@RequestBody Deployment entity) {
        return null;
    }

    @Override
    public Object putOne(@PathVariable("id") String s, @RequestBody Deployment entity) {
        return null;
    }

    @Override
    public Object patchOne(@PathVariable("id") String s, @RequestBody Deployment entity) {
        return null;
    }



}
