package com.example.quiz.service;

import java.util.Optional;

import com.example.quiz.entity.Quiz;

public interface QuizService {
    /** クイズ全件を取得します */
    Iterable<Quiz> selectAll();
    /** クイズを1件指定IDで取得します */
    Optional<Quiz> selectOneById(Integer id);
    /** クイズを1件ランダムで取得します */
    Optional<Quiz> selectOneRandom();
    /** クイズの正解/不正解を判断します */
    Boolean checkAnswwer(Integer id, Boolean answer);
    /** クイズを登録します */
    void insertQuiz(Quiz quiz);
    /** クイズを更新します */
    void updateQuiz(Quiz quiz);
    /** クイズを削除します */
    void deleteQuizById(Integer id);
}
