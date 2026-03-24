package com.example.takataka.controller;

import com.example.takataka.controller.form.TaskForm;
import com.example.takataka.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TaskController {
    @Autowired
    TaskService taskService;

    // TOP画面表示処理
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        // タスク全件取得
        List<TaskForm> taskData = taskService.findAllTask();
        // 画面遷移先を表示
        mav.addObject("tasks", taskData);
        mav.addObject("taskModel", new TaskForm());
        // 画面遷移先を表示
        mav.setViewName("top");
        return mav;
    }

}
