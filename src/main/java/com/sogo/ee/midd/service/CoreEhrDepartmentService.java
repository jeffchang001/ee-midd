package com.sogo.ee.midd.service;

import com.sogo.ee.midd.model.entity.CoreEhrDepartment;

import java.util.List;
import java.util.Optional;

/**
 * 核心 EHR 部門服務介面
 */
public interface CoreEhrDepartmentService {

    /**
     * 查詢所有部門資訊
     * @return 部門資訊列表
     */
    List<CoreEhrDepartment> findAll();

    /**
     * 根據部門代碼查詢部門資訊
     * @param orgCode 部門代碼
     * @return 部門資訊
     */
    Optional<CoreEhrDepartment> findByOrgCode(String orgCode);

    /**
     * 根據部門名稱查詢部門
     * @param orgName 部門名稱
     * @return 部門列表
     */
    List<CoreEhrDepartment> findByOrgNameContaining(String orgName);

    /**
     * 根據部門簡稱查詢部門
     * @param orgAbbrName 部門簡稱
     * @return 部門列表
     */
    List<CoreEhrDepartment> findByOrgAbbrNameContaining(String orgAbbrName);

    /**
     * 根據上級部門代碼查詢子部門
     * @param parentOrgCode 上級部門代碼
     * @return 子部門列表
     */
    List<CoreEhrDepartment> findByParentOrgCode(String parentOrgCode);

    /**
     * 根據主管員工編號查詢部門
     * @param managerEmployeeNo 主管員工編號
     * @return 部門
     */
    Optional<CoreEhrDepartment> findByManagerEmployeeNo(String managerEmployeeNo);

    /**
     * 查詢非虛擬部門
     * @param isVirtual 是否虛擬部門
     * @return 部門列表
     */
    List<CoreEhrDepartment> findByIsVirtual(Boolean isVirtual);
} 