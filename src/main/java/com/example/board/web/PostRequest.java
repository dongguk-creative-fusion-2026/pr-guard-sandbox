package com.example.board.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** 제목 길이는 posts.title VARCHAR(200) 에 맞춘다. */
public record PostRequest(@NotBlank @Size(max = 200) String title, @NotBlank String content) {
}
