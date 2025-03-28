package com.example.PureLift.service;

import com.example.PureLift.entity.Article;
import com.example.PureLift.repository.ArticleRepository;
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
        return articleRepository.findAll().stream().filter(Article::isPublished).toList();
    }
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Long id){
        return articleRepository.findAll().stream().filter(article -> article.getId() == id).findFirst();
    }

    public void changeArticleStatus(Long articleId, boolean published) {
        getArticleById(articleId).ifPresent(article -> article.setPublished(published));
    }
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }
}
