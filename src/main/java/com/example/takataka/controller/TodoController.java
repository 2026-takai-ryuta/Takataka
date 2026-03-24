package com.example.takataka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class TodoController {

    // 例として「http://localhost:8080/」にアクセスした時の処理
    @GetMapping("/")
    public ModelAndView showTopPage() {
        ModelAndView mav = new ModelAndView();
        // ① 現在の日付を取得し、「yyyy/MM/dd」形式にフォーマットする
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDate = today.format(formatter);

        // ② フォーマットした日付を "currentDate" という名前で画面に渡す
        mav.addObject("currentDate", formattedDate);

        // ③ 遷移先のHTMLファイル名を指定（拡張子不要）
        mav.setViewName("/top");

        return mav;
    }
}