package com.sogo.ee.midd.repository;

import com.sogo.ee.midd.model.entity.CoreEhrDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 核心 EHR 部門資料庫操作介面
 */
@Repository
public interface CoreEhrDepartmentRepository extends JpaRepository<CoreEhrDepartment, String> {
    
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