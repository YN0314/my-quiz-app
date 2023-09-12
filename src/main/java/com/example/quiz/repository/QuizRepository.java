package com.example.quiz.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.quiz.entity.Quiz;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Integer>{
    
    @Modifying
    @Query(value = "TRUNCATE TABLE quiz RESTART IDENTITY")
    void resetIdentity();
}
