package com.example.PureLift.config;

import com.example.PureLift.service.ArticleService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public ArticleService articleService() {
        return Mockito.mock(ArticleService.class);
    }
}
