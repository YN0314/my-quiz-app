package com.example.quiz.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizForm {
    /** クイズID */
    private Integer id;

    /** クイズの問題文 */
    @NotBlank
    private String question;

    /** クイズの解答 */
    private Boolean answer;

    /** クイズの出題者 */
    @NotBlank
    private String author;

    /** 新規登録フラグ */
    private Boolean isNew;
}
