package com.example.takataka.controller.form;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskForm {
    private Integer id;
    @NotBlank(message = "【タスクを入力してください】")
    @Size(max = 140, message = "【タスクは140文字以内で入力してください】")
    private String content;
    private Integer status;
    @NotBlank(message = "【期限を設定してください】")
    @FutureOrPresent(message = "【無効な日付です】")
    private LocalDateTime limitDate;
}
