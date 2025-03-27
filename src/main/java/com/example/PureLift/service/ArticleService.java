package com.example.PureLift.service;

import com.example.PureLift.entity.Article;
import com.example.PureLift.Repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {


    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    public List<Article> getPublishedArticles() {
        return articleRepository.getAllArticles().stream().filter(Article::isPublished).toList();
    }

    public Optional<Article> getArticleById(int id) {
        return articleRepository.getAllArticles().stream().filter(article -> article.getId() == id).findFirst();
    }

    public void changeArticleStatus(int articleId, boolean published) {
        getArticleById(articleId).ifPresent(article -> article.setPublished(published));
    }
}
