package com.example.PureLift.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    @Getter
    private boolean published;

    public Article( String title, String content, boolean published) {
        this.title = title;
        this.content = content;
        this.published = published;
    }

    public Article() {

    }


}
