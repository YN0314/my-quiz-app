package com.example.quiz.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;

@Table(name = "quiz")
@Builder
public record Quiz(
        @Id Integer id,
        String question,
        Boolean answer,
        String author) {
}
