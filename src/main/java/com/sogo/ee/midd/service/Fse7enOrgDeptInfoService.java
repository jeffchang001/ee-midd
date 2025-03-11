package com.sogo.ee.midd.service;

import com.sogo.ee.midd.model.entity.Fse7enOrgDeptInfo;

import java.util.List;
import java.util.Optional;

/**
 * 部門資訊服務介面
 */
public interface Fse7enOrgDeptInfoService {

    /**
     * 查詢所有部門資訊
     * @return 部門資訊列表
     */
    List<Fse7enOrgDeptInfo> findAll();

    /**
     * 根據組織代碼查詢部門資訊
     * @param orgCode 組織代碼
     * @return 部門資訊
     */
    Optional<Fse7enOrgDeptInfo> findByOrgCode(String orgCode);
} 