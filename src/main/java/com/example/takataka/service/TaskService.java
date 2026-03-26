package com.example.takataka.service;

import com.example.takataka.controller.form.TaskForm;
import com.example.takataka.repository.TaskRepository;
import com.example.takataka.repository.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    // レコード全件取得
    public List<TaskForm> findAllTask() {
        List<Task> results = taskRepository.findTop1000ByOrderByLimitDateAsc();
        List<TaskForm> tasks = setTaskForm(results);
        return tasks;
    }


    // DBから取得したデータをFormに設定
    private List<TaskForm> setTaskForm(List<Task> results) {
        List<TaskForm> tasks = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            TaskForm task = new TaskForm();
            Task result = results.get(i);
            task.setId(result.getId());
            task.setContent(result.getContent());
            task.setStatus(result.getStatus());
            task.setLimitDate(result.getLimitDate());
            tasks.add(task);
        }
        return tasks;
    }

    /*
     * 日付による絞り込み検索処理
     */
    public List<TaskForm> searchTask(String startDate, String endDate, Integer searchStatus, String searchContent) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        // ① 開始日が入力されている場合、「その日の 00:00:00」をセットする
        if (startDate != null && !startDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate);
            startDateTime = start.atStartOfDay();
        } else {
            // 空欄の場合は、適当な過去の日付を入れる
            startDateTime = LocalDateTime.of(1900, 1, 1, 0, 0);
        }

        // ② 終了日が入力されている場合、「その日の 23:59:59」をセットする
        if (endDate != null && !endDate.isEmpty()) {
            LocalDate end = LocalDate.parse(endDate);
            endDateTime = end.atTime(LocalTime.MAX);
        } else {
            // 空欄の場合は、適当な未来の日付を入れる
            endDateTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        }

        // ③ 時間を補完した日付を使ってDBを検索する
        List<Task> results = taskRepository.findBySearchCriteria(startDateTime, endDateTime, searchStatus, searchContent);

        // ④ Formに詰め替えてControllerに返す
        return setTaskForm(results);

    }

    /*
     * ステータス更新処理
     */
    public void updateStatus(Integer taskId, Integer status) {
        // ① JpaRepositoryの機能を使って、リクエストされたIDのタスクをDBから取得する
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task != null) {
            // ② 取得したタスクのステータスを、画面から送られてきた新しいステータスで上書きする
            task.setStatus(status);

            // ③ 詳細設計書の要件に合わせて、更新日時にも現在時刻をセットする
            task.setUpdatedDate(LocalDateTime.now());

            // ④ 保存する（IDが既に存在するため、Spring Data JPAが自動的にUPDATE文を実行します）
            taskRepository.save(task);
        }
    }



    // タスク削除
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);

    }

    // タスク追加
    public void saveTask(TaskForm reqTask) {
        Task saveTask = setTaskEntity(reqTask);
        taskRepository.save(saveTask);
    }

    // リクエストから取得した情報をEntitiyに設定
    private  Task setTaskEntity(TaskForm reqTask) {
        Task task = new Task();
        task.setContent(reqTask.getContent());
        task.setId(reqTask.getId());
        task.setStatus(reqTask.getStatus());
        task.setLimitDate(reqTask.getLimitDate());
        return  task;
    }

    /*
     * 編集対象のタスクを1件取得する処理
     */
    public TaskForm getTaskById(Integer taskId) {
        // ① JpaRepositoryの機能を使って、IDが一致するタスクを取得する
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task != null) {
            // ② 取得したTaskエンティティを、画面表示用のクラス（TaskFormなど）に変換する
            // 一旦リストに入れてから0番目を取り出す形にすると簡単に変換できます。
            return setTaskForm(List.of(task)).get(0);
        }
        return null;
    }

    /*
     * タスク更新処理
     */
    public void updateTask(Integer id, TaskForm taskModel) {
        // ① 更新対象のタスクをDBから取得する
        Task task = taskRepository.findById(id).orElse(null);

        if (task != null) {
            // ② 画面から送られてきた「タスク内容」と「タスク期限」で上書きする
            task.setContent(taskModel.getContent());
            task.setLimitDate(taskModel.getLimitDate());

            // ③ 更新日時を現在時刻で上書きする
            task.setUpdatedDate(LocalDateTime.now());

            // ④ 保存する
            taskRepository.save(task);
        }
    }
}
