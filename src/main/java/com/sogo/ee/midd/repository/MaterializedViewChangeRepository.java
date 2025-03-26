package com.sogo.ee.midd.repository;

import com.sogo.ee.midd.model.entity.MaterializedViewChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 實體化視圖變更資訊儲存庫介面
 */
@Repository
public interface MaterializedViewChangeRepository extends JpaRepository<MaterializedViewChange, Long> {
    
    /**
     * 根據視圖名稱和時間範圍查詢變更記錄
     *
     * @param viewName 視圖名稱
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 變更記錄列表
     */
    List<MaterializedViewChange> findByViewNameAndRefreshedAtBetween(
        String viewName, 
        LocalDateTime startTime, 
        LocalDateTime endTime
    );

    /**
     * 根據時間範圍查詢變更記錄
     *
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 變更記錄列表
     */
    List<MaterializedViewChange> findByRefreshedAtBetween(
        LocalDateTime startTime, 
        LocalDateTime endTime
    );
} 