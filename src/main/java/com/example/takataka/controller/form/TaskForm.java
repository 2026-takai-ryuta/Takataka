package com.example.takataka.controller.form;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskForm {
    private Integer id;

    // 【必須チェック(E0001) / 文字数チェック(E0003)】
    @NotBlank(message = "・タスクを入力してください")
    @Size(max = 140, message = "・タスクは140文字以内で入力してください")
    private String content;
    private Integer status;
    // 【必須チェック(E0002) / 日付チェック(E0004)】
    @NotNull(message = "・期限を設定してください")
    @FutureOrPresent(message = "・無効な日付です")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime limitDate;
}
