package com.example.takataka.controller;

import com.example.takataka.controller.form.TaskForm;
import com.example.takataka.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    /*
     * タスク編集画面初期表示
     */
    @GetMapping("/edit")
    public ModelAndView showEditPage(@RequestParam("taskId") Integer taskId) {
        ModelAndView mav = new ModelAndView();

        // ① Serviceを呼び出して、編集対象のタスク情報を取得する
        TaskForm task = taskService.getTaskById(taskId);

        // ② チームメンバーが作成した編集画面（task_edit.html）へ遷移先を指定する
        mav.setViewName("task_edit");

        // ③ 取得したタスク情報を、編集画面で表示できるように渡す
        mav.addObject("taskModel", task);

        return mav;
    }

    /*
     * タスク編集（更新）処理
     */
    @PostMapping("/task/update/{id}")
    public ModelAndView updateTask(@PathVariable("id") Integer id,
                                   @Validated @ModelAttribute("taskModel") TaskForm taskModel,
                                   BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();

        // ３．エラーメッセージリストのサイズをチェックする
        if (bindingResult.hasErrors()) {
            // エラーが1つ以上ある場合：編集画面（task_edit）へ戻す
            mav.setViewName("task_edit");
            return mav;
        }

        // ４．タスク更新処理
        // エラーが0件の場合はServiceの更新処理を呼び出す
        taskService.updateTask(id, taskModel);

        // ５．TOP画面表示処理
        // 更新成功時はTOP画面へリダイレクトする
        return new ModelAndView("redirect:/");
    }
}