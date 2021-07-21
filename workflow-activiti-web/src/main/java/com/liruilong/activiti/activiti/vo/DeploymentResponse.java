package com.liruilong.activiti.activiti.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.activiti.engine.repository.Deployment;

import java.util.Date;

public class DeploymentResponse {

  private String id;
  private String name;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date deploymentTime;
  private String category;
  private String tenantId;
  
  public DeploymentResponse(Deployment deployment) {
    setId(deployment.getId());
    setName(deployment.getName());
    setDeploymentTime(deployment.getDeploymentTime());
    setCategory(deployment.getCategory());
    setTenantId(deployment.getTenantId());
  }
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public  Date getDeploymentTime() {
    return deploymentTime;
  }
  public void setDeploymentTime( Date deploymentTime) {
    this.deploymentTime = deploymentTime;
  }
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
  public void setTenantId(String tenantId) {
      this.tenantId = tenantId;
  }
  public String getTenantId() {
	  return tenantId;
  }
}