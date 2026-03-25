package com.example.takataka.service;

import com.example.takataka.controller.form.TaskForm;
import com.example.takataka.repository.TaskRepository;
import com.example.takataka.repository.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return  tasks;
    }


    // タスク削除
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);

    }
}
