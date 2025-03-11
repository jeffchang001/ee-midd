package com.sogo.ee.midd.service;

import com.sogo.ee.midd.model.entity.Fse7enOrgDeptStruct;

import java.util.List;
import java.util.Optional;

/**
 * 部門結構資訊服務介面
 */
public interface Fse7enOrgDeptStructService {

    /**
     * 查詢所有部門結構資訊
     * @return 部門結構資訊列表
     */
    List<Fse7enOrgDeptStruct> findAll();

    /**
     * 根據組織代碼查詢部門結構資訊
     * @param orgCode 組織代碼
     * @return 部門結構資訊
     */
    Optional<Fse7enOrgDeptStruct> findByOrgCode(String orgCode);

    /**
     * 根據父組織代碼查詢部門結構資訊
     * @param parentOrgCode 父組織代碼
     * @return 部門結構資訊列表
     */
    List<Fse7enOrgDeptStruct> findByParentOrgCode(String parentOrgCode);

    /**
     * 根據頂層組織代碼查詢部門結構資訊
     * @param topOrgtreeCode 頂層組織代碼
     * @return 部門結構資訊列表
     */
    List<Fse7enOrgDeptStruct> findByTopOrgtreeCode(String topOrgtreeCode);
} 