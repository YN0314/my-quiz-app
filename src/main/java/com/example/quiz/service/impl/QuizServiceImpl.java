package com.example.quiz.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.service.QuizService;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository repo;

    @Override
    public Iterable<Quiz> selectAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Quiz> selectOneById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Quiz> selectOneRandom() {
        Integer randId = repo.getRandomId();
        if (ObjectUtils.isEmpty(randId)) {
            return Optional.empty();
        }
        return repo.findById(randId);
    }

    @Override
    public Boolean checkAnswer(Integer id, Boolean answer) {
        Optional<Quiz> quiz = repo.findById(id);
        if (quiz.isPresent()) {
            return quiz.get().answer().equals(answer);
        }
        return false;
    }

    @Override
    public void insertQuiz(Quiz quiz) {
        repo.save(quiz);
    }

    @Override
    public void updateQuiz(Quiz quiz) {
        repo.save(quiz);
    }

    @Override
    public void deleteQuizById(Integer id) {
        repo.deleteById(id);
    }
}
