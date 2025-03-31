package com.example.PureLift.controller;


import com.example.PureLift.entity.Article;
import com.example.PureLift.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping("/published")
    public ResponseEntity<List<Article>> getPublishedArticles() {
        List<Article> articles = articleService.getPublishedArticles();
        return ResponseEntity.status(200).body(articles);
    }
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.status(200).body(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Article>> getArticleById(@PathVariable Long id) {
        try {
            Optional<Article> article = articleService.getArticleById(id);
            return ResponseEntity.status(200).body(article);
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody Article article) {
        try {
            Article createdArticle = articleService.createArticle(article);
            return ResponseEntity.status(201).body(createdArticle);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas tworzenia artykułu: " + e.getMessage());
        }
    }
}
