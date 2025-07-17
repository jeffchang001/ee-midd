package com.sogo.ee.midd.repository;

import com.sogo.ee.midd.model.entity.CoreEhrEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 核心 EHR 員工資料庫操作介面
 */
@Repository
public interface CoreEhrEmployeeRepository extends JpaRepository<CoreEhrEmployee, String> {
    
    /**
     * 根據姓名查詢員工
     * @param fullName 姓名
     * @return 員工列表
     */
    List<CoreEhrEmployee> findByFullNameContaining(String fullName);
    
    /**
     * 根據電子郵件地址查詢員工
     * @param emailAddress 電子郵件地址
     * @return 員工
     */
    Optional<CoreEhrEmployee> findByEmailAddress(String emailAddress);
    
    /**
     * 根據部門代碼查詢員工
     * @param formulaOrgCode 部門代碼
     * @return 員工列表
     */
    List<CoreEhrEmployee> findByFormulaOrgCode(String formulaOrgCode);
    
    /**
     * 根據職稱代碼查詢員工
     * @param jobTitleCode 職稱代碼
     * @return 員工列表
     */
    List<CoreEhrEmployee> findByJobTitleCode(String jobTitleCode);
    
    /**
     * 根據職級代碼查詢員工
     * @param jobGradeCode 職級代碼
     * @return 員工列表
     */
    List<CoreEhrEmployee> findByJobGradeCode(String jobGradeCode);
    
    /**
     * 根據在職狀態查詢員工
     * @param employedStatus 在職狀態
     * @return 員工列表
     */
    List<CoreEhrEmployee> findByEmployedStatus(String employedStatus);
    
    /**
     * 根據主管員工編號查詢下屬員工
     * @param managerEmployeeNo 主管員工編號
     * @return 下屬員工列表
     */
    List<CoreEhrEmployee> findByManagerEmployeeNo(String managerEmployeeNo);
} 