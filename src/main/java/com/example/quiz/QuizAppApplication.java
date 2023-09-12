package com.example.quiz;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizRepository;


@SpringBootApplication
public class QuizAppApplication {

    @Autowired
    private QuizRepository repo;

    public static void main(String[] args) {
        SpringApplication.run(QuizAppApplication.class, args)
        .getBean(QuizAppApplication.class).execute();
    }

    private void execute() {
//        // 登録
//        setup();
//        // 全件取得
//        showList();
//        // 1件取得
//        showOne(1);
//        // 更新処理
//        updateQuiz(1);
//        showOne(1);
//        // 削除処理
//        deleteQuiz(2);
    }

    private void setup() {
        // テーブルクリア
        repo.deleteAll();
        // シリアル採番値初期化
        repo.resetIdentity();
        initRecords().stream().forEach(entity -> repo.save(entity));
    }

    private void showList() {
        System.out.println("--- 全件取得開始 ---");
        repo.findAll().forEach(quiz -> System.out.println(quiz));
        System.out.println("--- 全件取得完了 ---");
    }

    private void showOne(Integer id) {
        System.out.println("--- 1件取得開始 ---");
        repo.findById(id).ifPresentOrElse(entity -> {
            System.out.println(entity);
        }, () -> {
            System.out.println("該当する問題が存在しません・・・");
        });
        System.out.println("--- 1件取得終了 ---");
    }

    private void updateQuiz(Integer id) {
        System.out.println("--- 更新処理開始 ---");
        Quiz quiz1 = repo.findById(id).orElseThrow();
        repo.save(Quiz.builder()
                .id(quiz1.id())
                .question("「スプリング」はフレームワークですか？")
                .answer(quiz1.answer())
                .author(quiz1.author()).build());
        System.out.println("--- 更新処理終了 ---");
    }

    private void deleteQuiz(Integer id) {
        System.out.println("--- 削除処理開始 ---");
        repo.deleteById(id);
        System.out.println("--- 削除処理終了 ---");
    }

    /**
     * 登録レコード生成
     * @return
     */
    private List<Quiz> initRecords() {
        return Stream.of(
                // レコード１
                Quiz.builder()
                .question("「Spring」はフレームワークですか？")
                .answer(true)
                .author("sys").build(),
                // レコード２
                Quiz.builder()
                .question("「Spring MVC」はバッチ機能を提供しますか？")
                .answer(false)
                .author("sys").build()).toList();
    }
}
