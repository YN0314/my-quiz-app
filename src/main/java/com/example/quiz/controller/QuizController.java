package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;

@Controller
@RequestMapping("/quiz")
public class QuizController {
    @Autowired
    private QuizService service;

    @ModelAttribute
    public QuizForm setUpForm() {
        return QuizForm.builder()
                .answer(true) // default
                .build();
    }

    /** クイズ登録・一覧画面 */
    @GetMapping
    public String showList(QuizForm quizForm, Model model) {
        // 新規登録設定
        quizForm.setIsNew(true);
        // 一覧取得
        Iterable<Quiz> list = service.selectAll();
        // 表示データ格納
        model.addAttribute("list", list);
        model.addAttribute("title", "登録用フォーム");
        return "crud";
    }

    /** クイズ登録 */
    @PostMapping("/insert")
    public String insert(@Validated QuizForm quizForm, BindingResult result, Model model, RedirectAttributes attr) {
        Quiz quiz = Quiz.builder()
                .question(quizForm.getQuestion())
                .answer(quizForm.getAnswer())
                .author(quizForm.getAuthor()).build();
        if (result.hasErrors()) {
            // バリデーションエラー
            return showList(quizForm, model);
        }
        // DB登録
        service.insertQuiz(quiz);
        attr.addFlashAttribute("complete", "登録が完了しました");
        return "redirect:/quiz";
    }
}
