package com.example.PureLift.controller;

import com.example.PureLift.entity.Article;
import com.example.PureLift.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.PureLift.config.TestConfig;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@ActiveProfiles("test")
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
    }

    @Test
    void injectedArticleService_ShouldBeMock() {
        Assertions.assertTrue(Mockito.mockingDetails(articleService).isMock());
    }

    @Test
    @WithMockUser
    void getPublishedArticles_ShouldReturnArticlesList() throws Exception {
        when(articleService.getPublishedArticles()).thenReturn(Arrays.asList(testArticle));

        mockMvc.perform(get("/api/articles/published"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(testArticle.getId()))
                .andExpect(jsonPath("$[0].title").value(testArticle.getTitle()))
                .andExpect(jsonPath("$[0].content").value(testArticle.getContent()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllArticles_ShouldReturnAllArticles() throws Exception {
        when(articleService.getAllArticles()).thenReturn(Arrays.asList(testArticle));

        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(testArticle.getId()))
                .andExpect(jsonPath("$[0].title").value(testArticle.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getArticleById_WhenArticleExists_ShouldReturnArticle() throws Exception {
        when(articleService.getArticleById(1L)).thenReturn(Optional.of(testArticle));

        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testArticle.getId()))
                .andExpect(jsonPath("$.title").value(testArticle.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getArticleById_WhenArticleDoesNotExist_ShouldReturn404() throws Exception {
        when(articleService.getArticleById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/articles/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createArticle_ShouldReturnCreatedArticle() throws Exception {
        when(articleService.createArticle(any(Article.class))).thenReturn(testArticle);

        mockMvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testArticle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testArticle.getId()))
                .andExpect(jsonPath("$.title").value(testArticle.getTitle()));
    }

    @Test
    @WithMockUser
    void getPublishedArticles_ShouldReturnSortedById() throws Exception {
        Article article1 = new Article();
        article1.setId(2L);
        article1.setTitle("Second Article");
        article1.setContent("Test Content 2");
        article1.setPublished(true);
        
        Article article2 = new Article();
        article2.setId(1L);
        article2.setTitle("First Article");
        article2.setContent("Test Content 1");
        article2.setPublished(true);

        when(articleService.getPublishedArticles()).thenReturn(Arrays.asList(article2, article1));

        mockMvc.perform(get("/api/articles/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
}
