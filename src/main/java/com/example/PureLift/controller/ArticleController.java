package com.example.PureLift.controller;


import com.example.PureLift.entity.Article;
import com.example.PureLift.exception.ArticleNotFoundException;
import com.example.PureLift.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
        return ResponseEntity.ok(article);
    }
    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody Article article) {
        Article createdArticle = articleService.createArticle(article);
        return ResponseEntity.status(201).body(createdArticle);
    }
}
