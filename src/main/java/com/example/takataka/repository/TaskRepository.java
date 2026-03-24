package com.example.takataka.repository;

import com.example.takataka.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findTop1000ByOrderByLimitDateAsc();
}
