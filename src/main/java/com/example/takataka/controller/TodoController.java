package com.example.takataka.controller;

import com.example.takataka.controller.form.TaskForm;
import com.example.takataka.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class TodoController {
    @Autowired
    TaskService taskService;

    // 例として「http://localhost:8080/」にアクセスした時の処理
    @GetMapping("/")
    public ModelAndView showTopPage() {
        ModelAndView mav = new ModelAndView();

        // タスク全件取得
        List<TaskForm> taskData = taskService.findAllTask();
        // 画面遷移先を表示
        mav.addObject("tasks", taskData);
        mav.addObject("taskModel", new TaskForm());

        // ① 現在の日付を取得し、「yyyy/MM/dd」形式にフォーマットする
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDate = today.format(formatter);

        // ② フォーマットした日付を "currentDate" という名前で画面に渡す
        mav.addObject("currentDate", formattedDate);

        // TaskServiceの全件取得メソッドを呼び出す
        List<TaskForm> contentData = taskService.findAllTask();

        // "contents" という名前で、取得したタスクのリストをHTMLに渡す
        mav.addObject("contents", contentData);

        // ③ 遷移先のHTMLファイル名を指定（拡張子不要）
        mav.setViewName("/top");

        return mav;
    }

    /*
     * 日付検索処理
     */
    @GetMapping("/search")
    public ModelAndView search(@RequestParam(name = "startDate", required = false) String startDate,
                               @RequestParam(name = "endDate", required = false) String endDate,
                               @RequestParam(name = "searchStatus", required = false) Integer searchStatus,
                               @RequestParam(name = "searchContent", required = false) String searchContent
    ) {
        ModelAndView mav = new ModelAndView();

        // Serviceの検索メソッドを呼び出す
        List<TaskForm> contentData = taskService.searchTask(startDate, endDate, searchStatus, searchContent);

        mav.setViewName("top");
        mav.addObject("contents", contentData);

        // 検索後も入力した日付を画面のフォームに残すための処理
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("searchStatus", searchStatus);
        mav.addObject("searchContent", searchContent);

        return mav;
    }

    /*
     * ステータス変更処理
     */
    @PostMapping("/updateStatus")
    public ModelAndView updateStatus(@RequestParam("taskId") Integer taskId,
                                     @RequestParam("status") Integer status) {

        // ① Serviceのステータス更新処理を呼び出す
        taskService.updateStatus(taskId, status);

        // ② TOP画面のURLを設定し、リダイレクト処理をする
        return new ModelAndView("redirect:/");
    }

    // タスク削除
    @PostMapping("/delete")
    public ModelAndView deleteTask(@RequestParam("taskId") Integer id) {
        //
        taskService.deleteTask(id);
        //
        return new ModelAndView("redirect:/");
    }
}