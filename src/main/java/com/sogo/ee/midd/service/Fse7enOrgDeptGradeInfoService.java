package com.sogo.ee.midd.service;

import com.sogo.ee.midd.model.entity.Fse7enOrgDeptGradeInfo;

import java.util.List;
import java.util.Optional;

/**
 * 部門等級資訊服務介面
 */
public interface Fse7enOrgDeptGradeInfoService {

    /**
     * 查詢所有部門等級資訊
     * @return 部門等級資訊列表
     */
    List<Fse7enOrgDeptGradeInfo> findAll();

    /**
     * 根據等級ID查詢部門等級資訊
     * @param gradeId 等級ID
     * @return 部門等級資訊
     */
    Optional<Fse7enOrgDeptGradeInfo> findByGradeId(String gradeId);
} 