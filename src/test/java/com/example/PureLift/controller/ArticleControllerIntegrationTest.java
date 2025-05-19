package com.example.PureLift.controller;

import com.example.PureLift.entity.Article;
import com.example.PureLift.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.PureLift.config.TestConfig;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
    properties = "spring.config.location=classpath:application-test.properties",
    classes = {TestConfig.class}
)
@AutoConfigureMockMvc
class ArticleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    private Article testArticle;

    @BeforeEach
    void setUp() {
        testArticle = new Article();
        testArticle.setId(1L);
        testArticle.setTitle("Test Article");
        testArticle.setContent("Test Content");
        testArticle.setPublished(true);
    }    @Test
    @WithMockUser
    void getPublishedArticles_ShouldReturnArticles() throws Exception {
        doReturn(Arrays.asList(testArticle))
            .when(articleService).getPublishedArticles();

        mockMvc.perform(get("/api/articles/published"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(testArticle.getId()))
            .andExpect(jsonPath("$[0].title").value(testArticle.getTitle()))
            .andExpect(jsonPath("$[0].content").value(testArticle.getContent()));
    }    @Test
    @WithMockUser
    void getAllArticles_ShouldReturnAllArticles() throws Exception {
        doReturn(Arrays.asList(testArticle))
            .when(articleService).getAllArticles();

        mockMvc.perform(get("/api/articles"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(testArticle.getId()))
            .andExpect(jsonPath("$[0].title").value(testArticle.getTitle()))
            .andExpect(jsonPath("$[0].content").value(testArticle.getContent()));
    }    @Test
    @WithMockUser
    void getArticleById_WhenArticleExists_ShouldReturnArticle() throws Exception {
        doReturn(Optional.of(testArticle))
            .when(articleService).getArticleById(1L);

        mockMvc.perform(get("/api/articles/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testArticle.getId()))
            .andExpect(jsonPath("$.title").value(testArticle.getTitle()))
            .andExpect(jsonPath("$.content").value(testArticle.getContent()));
    }    @Test
    @WithMockUser
    void getArticleById_WhenArticleDoesNotExist_ShouldReturn404() throws Exception {
        doReturn(Optional.empty())
            .when(articleService).getArticleById(1L);

        mockMvc.perform(get("/api/articles/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createArticle_ShouldReturnCreatedArticle() throws Exception {
        when(articleService.createArticle(any(Article.class)))
            .thenReturn(testArticle);

        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testArticle)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testArticle.getId()))
            .andExpect(jsonPath("$.title").value(testArticle.getTitle()))
            .andExpect(jsonPath("$.content").value(testArticle.getContent()));
    }
}
