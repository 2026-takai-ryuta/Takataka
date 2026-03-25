package com.example.takataka.repository;

import com.example.takataka.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findTop1000ByOrderByLimitDateAsc();

    // limitDate（タスク期限）でBetween検索し、昇順（Asc）で並び替える
    //List<Task> findByLimitDateBetweenOrderByLimitDateAsc(LocalDateTime startDateTime, LocalDateTime endDateTime);
    // ↓ 新しく追加する検索メソッド（日付、ステータス、タスク内容で絞り込み）
    @Query("SELECT t FROM Task t WHERE t.limitDate BETWEEN :startDate AND :endDate " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:content IS NULL OR :content = '' OR t.content = :content) " +
            "ORDER BY t.limitDate ASC")
    List<Task> findBySearchCriteria(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") Integer status,
            @Param("content") String content
    );
}
