package com.example.PureLift.controller;


import com.example.PureLift.entity.Article;
import com.example.PureLift.exception.ArticleNotFoundException;
import com.example.PureLift.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping("/published")
    public ResponseEntity<List<Article>> getPublishedArticles() {
        List<Article> articles = articleService.getPublishedArticles();
        return ResponseEntity.ok(articles);
    }
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createArticle(@Valid @RequestBody Article article) {
        if (article.getTitle() == null || article.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Title cannot be empty");
        }
        Article createdArticle = articleService.createArticle(article);
        return ResponseEntity.status(201).body(createdArticle);
    }
}
