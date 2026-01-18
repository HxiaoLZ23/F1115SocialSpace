package com.form1115.f1115.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * AI配置类
 */
@Configuration
public class AIConfig {
    
    @Value("${dashscope.apiKey}")
    private String apiKey;
    
    @Value("${dashscope.model}")
    private String model;
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getModel() {
        return model;
    }
}
