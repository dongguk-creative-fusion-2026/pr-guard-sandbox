package com.example.board.web;

import jakarta.validation.constraints.NotBlank;

public record PostRequest(@NotBlank String title, @NotBlank String content) {
}
