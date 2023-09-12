package com.example.quiz.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.quiz.entity.Quiz;

public interface QuizRepository extends CrudRepository<Quiz, Integer>{
    /** シリアルIDの採番をリセットします */
    @Modifying
    @Query(value = "TRUNCATE TABLE quiz RESTART IDENTITY")
    void resetIdentity();

    /** ランダムなIDを一件取得します */
    @Query(value = "SELECT id FROM quiz ORDER BY RANDOM() limit 1")
    Integer getRandomId();
}
