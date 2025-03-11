package com.sogo.ee.midd.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 實體化視圖變更資訊 DTO
 * 用於表示 get_materialized_view_changes_by_date 函數的查詢結果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterializedViewChangeDto {

    /**
     * 視圖名稱
     */
    private String viewName;

    /**
     * 更新時間
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime refreshedAt;

    /**
     * 變更記錄數量
     */
    private Integer diffCount;

    /**
     * 變更詳細資訊 (JSONB)
     */
    private Object diffDetails;
} 