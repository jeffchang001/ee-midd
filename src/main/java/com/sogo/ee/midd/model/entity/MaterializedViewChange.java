package com.sogo.ee.midd.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 實體化視圖變更資訊實體類
 */
@Data
@Entity
@Table(name = "materialized_view_changes")
public class MaterializedViewChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "view_name", nullable = false)
    private String viewName;

    @Column(name = "refreshed_at", nullable = false)
    private LocalDateTime refreshedAt;

    @Column(name = "diff_count")
    private Integer diffCount;

    @Column(name = "diff_details", columnDefinition = "jsonb")
    private String diffDetails;
} 