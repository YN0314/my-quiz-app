package com.example.quiz;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.quiz.entity.Quiz;
import com.example.quiz.service.QuizService;


@SpringBootApplication
public class QuizAppApplication {

    @Autowired
    private QuizService service;

    public static void main(String[] args) {
        SpringApplication.run(QuizAppApplication.class, args)
        .getBean(QuizAppApplication.class).execute();
    }

    private void execute() {
        // 登録
        setup();
//        // 全件取得
//        showList();
//        // 1件取得
//        showOne(1);
//        // 更新処理
//        updateQuiz(1);
//        showOne(1);
//        // 削除処理
//        deleteQuiz(2);
//        // クイズを実行
//        doQuiz();
    }

    private void setup() {
        // テーブルクリア
        service.deleteAll();
        // シリアル採番値初期化
        service.resetIdentity();
        initRecords().stream().forEach(entity -> service.insertQuiz(entity));
    }

    private void showList() {
        System.out.println("--- 全件取得開始 ---");
        service.selectAll().forEach(quiz -> System.out.println(quiz));
        System.out.println("--- 全件取得完了 ---");
    }

    private void showOne(Integer id) {
        System.out.println("--- 1件取得開始 ---");
        service.selectOneById(id).ifPresentOrElse(entity -> {
            System.out.println(entity);
        }, () -> {
            System.out.println("該当する問題が存在しません・・・");
        });
        System.out.println("--- 1件取得終了 ---");
    }

    private void updateQuiz(Integer id) {
        System.out.println("--- 更新処理開始 ---");
        Quiz quiz1 = service.selectOneById(id).orElseThrow();
        service.insertQuiz(Quiz.builder()
                .id(quiz1.id())
                .question("「スプリング」はフレームワークですか？")
                .answer(quiz1.answer())
                .author(quiz1.author()).build());
        System.out.println("--- 更新処理終了 ---");
    }

    private void deleteQuiz(Integer id) {
        System.out.println("--- 削除処理開始 ---");
        service.deleteQuizById(id);
        System.out.println("--- 削除処理終了 ---");
    }

    private void doQuiz() {
        System.out.println("--- クイズ1件取得開始 ---");
        Optional<Quiz> optQuiz = service.selectOneRandom();
        optQuiz.ifPresentOrElse(quiz -> {
            System.out.println(quiz);
            // 解答確認
            Boolean myAnswer = false;
            checkAnswer(optQuiz.get(), myAnswer);
        }, () -> {
            System.out.println("該当する問題が存在しません・・・");
        });
        System.out.println("--- クイズ1件取得終了 ---");
    }

    private void checkAnswer(Quiz quiz, Boolean myAns) {
        System.out.println("設問：" + quiz.question());
        System.out.println("あなたの回答：" + (myAns ? "○" : "×"));
        if (service.checkAnswer(quiz.id(), myAns)) {
            System.out.println("正解です！");
        } else {
            System.out.println("不正解です・・・");
        }
    }
    
    /**
     * 登録レコード生成
     * @return
     */
    private List<Quiz> initRecords() {
        return Stream.of(
                // レコード１
                Quiz.builder()
                .question("「Java」はオブジェクト指向言語である。")
                .answer(true)
                .author("sys").build(),
                // レコード２
                Quiz.builder()
                .question("「Spring Data」はデータアクセスに対する機能を提供する。")
                .answer(true)
                .author("sys").build(),
                // レコード３
                Quiz.builder()
                .question("プログラムが沢山配置されているサーバーのことを「ライブラリ」という。")
                .answer(false)
                .author("sys").build(),
                // レコード４
                Quiz.builder()
                .question("「@Component」はインスタンス生成アノテーションである。")
                .answer(true)
                .author("sys").build(),
                // レコード５
                Quiz.builder()
                .question("「Spring MVC」が実装している「デザインパターン」で"
                        + "全てのリクエストを１つのコントローラーで受け取るパターンは"
                        + "「シングルコントローラ・パターン」である。")
                .answer(false)
                .author("sys").build()).toList();
    }
}
