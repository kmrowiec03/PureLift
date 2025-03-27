package com.example.PureLift.controller;


import com.example.PureLift.entity.Article;
import com.example.PureLift.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping
    public ResponseEntity<List<Article>> getPublishedArticles() {
            return ResponseEntity.ok(articleService.getPublishedArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(articleService.getArticleById(id).get());
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
}
