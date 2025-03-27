package com.example.PureLift.repository;

import com.example.PureLift.entity.Article;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private static final List<Article> articles = new ArrayList<>();

    static {
        articles.add(new Article(1, "Low body training", "dvsdfsdfsfdsfdsfds",  true));
        articles.add(new Article(2, "Upper body training", "aaaaaaa aaaaaaaa aaaaaaaaaaaaaa aaaa aaaaaaaa aaaaa", false));
    }


    public List<Article> getAllArticles() {
        return articles;
    }

}
