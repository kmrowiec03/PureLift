package com.example.PureLift.Entity;

public class Article {
    private int id;
    private String title;
    private String content;
    private boolean published;

    public Article(int id, String title, String content, boolean published) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.published = published;
    }

    public int getId() {
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
}
