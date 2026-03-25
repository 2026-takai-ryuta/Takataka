package com.example.takataka.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String content;

    @Column
    private Integer status;

    @Column(name = "limit_date")
    private LocalDateTime limitDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    public void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now; // 作成日時に現在時刻をセット
        this.updatedDate = now; // 更新日時にも現在時刻をセット
    }

    /**
     * DBのデータが更新（UPDATE）される直前に自動で実行される処理
     */
    @PreUpdate
    public void onPreUpdate() {
        this.updatedDate = LocalDateTime.now(); // 更新日時だけ現在時刻で上書き
    }
}
