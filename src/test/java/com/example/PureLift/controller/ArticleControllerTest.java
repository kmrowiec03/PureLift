package com.example.PureLift.controller;

import com.example.PureLift.entity.Article;
import com.example.PureLift.exception.ArticleNotFoundException;
import com.example.PureLift.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    private Article testArticle;
    private List<Article> testArticles;

    @BeforeEach
    void setUp() {
        testArticle = new Article();
        testArticle.setId(1L);
        testArticle.setTitle("Test Article");
        testArticle.setContent("Test Content");
        
        testArticles = Arrays.asList(testArticle);
    }

    @Test
    void getPublishedArticles_ShouldReturnArticlesList() {
        when(articleService.getPublishedArticles()).thenReturn(testArticles);

        ResponseEntity<List<Article>> response = articleController.getPublishedArticles();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(testArticles, response.getBody());
        verify(articleService).getPublishedArticles();
    }

    @Test
    void getAllArticles_ShouldReturnAllArticles() {
        when(articleService.getAllArticles()).thenReturn(testArticles);

        ResponseEntity<List<Article>> response = articleController.getAllArticles();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(testArticles, response.getBody());
        verify(articleService).getAllArticles();
    }

    @Test
    void getArticleById_WhenArticleExists_ShouldReturnArticle() {
        when(articleService.getArticleById(1L)).thenReturn(Optional.of(testArticle));

        ResponseEntity<Article> response = articleController.getArticleById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(testArticle, response.getBody());
        verify(articleService).getArticleById(1L);
    }

    @Test
    void getArticleById_WhenArticleDoesNotExist_ShouldThrowException() {
        when(articleService.getArticleById(1L)).thenReturn(Optional.empty());

        assertThrows(ArticleNotFoundException.class, () -> {
            articleController.getArticleById(1L);
        });
        
        verify(articleService).getArticleById(1L);
    }

    @Test
    void createArticle_ShouldReturnCreatedArticle() {
        when(articleService.createArticle(any(Article.class))).thenReturn(testArticle);

        ResponseEntity<?> response = articleController.createArticle(testArticle);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(testArticle, response.getBody());
        verify(articleService).createArticle(any(Article.class));
    }
}
