package com.example.quiz.controller;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;

@Controller
@RequestMapping("/quiz")
public class QuizController {
    @Autowired
    private QuizService service;
    @Autowired
    private MessageSource messageSource;

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
        model.addAttribute("title", getMessage("label.quiz.insert.title"));
        return "crud";
    }

    /** クイズプレイ */
    @GetMapping("/play")
    public String showQuiz(QuizForm quizForm, Model model) {
        model.addAttribute("quizForm", quizForm);
        model.addAttribute("title", getMessage("label.quiz.play.subtitle"));
        service.selectOneRandom().ifPresentOrElse(quiz -> {
            model.addAttribute("quizForm", makeQuizForm(quiz));
        }, () -> {
            model.addAttribute("msg", getMessage("label.msg.answer.nothingQuestion"));
        });
        return "play";
    }

    /** クイズの解答チェック */
    @PostMapping("/check")
    public String checkQuiz(QuizForm quizForm, @RequestParam Boolean answer, Model model) {
        if (service.checkAnswer(quizForm.getId(), answer)) {
            model.addAttribute("msg", getMessage("label.msg.answer.success"));
        } else {
            model.addAttribute("msg", getMessage("label.msg.answer.failed"));
        }
        return "answer";
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
        attr.addFlashAttribute("complete", getMessage("label.quiz.insert.complete"));
        return "redirect:/quiz";
    }

    /** クイズ更新画面表示 */
    @GetMapping("/{id}")
    public String showUpdate(QuizForm quizForm, @PathVariable Integer id, Model model) {
        Optional<QuizForm> updateQuizForm = service.selectOneById(id)
                .map(quiz -> makeQuizForm(quiz));
        if (updateQuizForm.isPresent()) {
            quizForm = updateQuizForm.get();
        }
        makeUpdateModel(quizForm, model);
        return "crud";
    }

    /** クイズ更新 */
    @PostMapping("/update")
    public String update(@Validated QuizForm quizForm, BindingResult result, Model model, RedirectAttributes attr) {
        Quiz quiz = makeQuiz(quizForm);
        if (result.hasErrors()) {
            // バリデーションエラー
            makeUpdateModel(quizForm, model);
            return "crud";
        } else {
            service.updateQuiz(quiz);
            attr.addFlashAttribute("complete", getMessage("label.quiz.update.complete"));
            return "redirect:/quiz/" + quiz.id();
        }
    }

    /** クイズ削除 */
    @PostMapping("/delete")
    public String delete(@RequestParam("id") String id, Model model, RedirectAttributes attr) {
        service.deleteQuizById(Integer.parseInt(id));
        attr.addFlashAttribute("delcomplete", getMessage("label.quiz.delete.complete", new String[] {id}));
        return "redirect:/quiz";
    }

    private void makeUpdateModel(QuizForm quizForm, Model model) {
        model.addAttribute("id", quizForm.getId());
        quizForm.setIsNew(false);
        model.addAttribute("quizForm", quizForm);
        model.addAttribute("title", getMessage("label.quiz.update.title"));
    }

    private QuizForm makeQuizForm(Quiz quiz) {
        return QuizForm.builder()
                .id(quiz.id())
                .question(quiz.question())
                .answer(quiz.answer())
                .author(quiz.author())
                .isNew(false)
                .build();
    }

    private Quiz makeQuiz(QuizForm quizForm) {
        return Quiz.builder()
                .id(quizForm.getId())
                .question(quizForm.getQuestion())
                .answer(quizForm.getAnswer())
                .author(quizForm.getAuthor())
                .build();
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    private String getMessage(String key, Object[] args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}
