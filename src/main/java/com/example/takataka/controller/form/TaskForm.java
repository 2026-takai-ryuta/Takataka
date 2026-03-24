package com.example.takataka.controller.form;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskForm {
    private Integer id;
    @NotBlank(message = "【タスクを入力してください】")
    private String content;
    private Integer status;
    private LocalDateTime limitDate;
}
