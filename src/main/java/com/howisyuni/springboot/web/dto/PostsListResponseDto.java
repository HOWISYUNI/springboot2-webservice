package com.howisyuni.springboot.web.dto;

import com.howisyuni.springboot.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private String ccontent;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.ccontent = entity.getContent();
        this.modifiedDate = entity.getModifiedDate();
    }
}
