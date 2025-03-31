package com.example.PureLift.entity;

import jakarta.persistence.*;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private boolean published;

    public Article( String title, String content, boolean published) {
        this.title = title;
        this.content = content;
        this.published = published;
    }

    public Article() {

    }

    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
