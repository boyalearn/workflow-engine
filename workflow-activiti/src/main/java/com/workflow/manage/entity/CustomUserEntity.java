package com.workflow.manage.entity;

import lombok.Getter;
import lombok.Setter;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class CustomUserEntity extends UserEntity implements PersistentObject {
    private int age;

    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    @Override
    public Object getPersistentState() {
        Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("id", this.id);
        persistentState.put("age", this.age);
        return persistentState;
    }
}
