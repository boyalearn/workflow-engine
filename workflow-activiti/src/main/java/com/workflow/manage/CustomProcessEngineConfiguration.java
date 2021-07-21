package com.workflow.manage;

import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;

import java.io.InputStream;

public class CustomProcessEngineConfiguration extends StandaloneProcessEngineConfiguration {
    public CustomProcessEngineConfiguration() {
        setDbIdentityUsed(false);
    }

    @Override
    protected InputStream getMyBatisXmlConfigurationSteam() {
        return getResourceAsStream("activiti-mapping/mappings.xml");
    }
}
