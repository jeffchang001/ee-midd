package com.sogo.ee.midd.service;

import com.sogo.ee.midd.model.dto.MaterializedViewChangeDto;

import java.time.LocalDate;
import java.util.List;

/**
 * 實體化視圖變更資訊服務介面
 */
public interface MaterializedViewChangeService {

    /**
     * 根據日期和視圖名稱查詢實體化視圖變更資訊
     *
     * @param date     查詢日期
     * @param viewName 視圖名稱 (可選)
     * @return 實體化視圖變更資訊列表
     */
    List<MaterializedViewChangeDto> getViewChangesByDate(LocalDate date, String viewName);

    /**
     * 重新整理所有的 materialized views
     */
    void refreshAllMaterializedViews();
} 