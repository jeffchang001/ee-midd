package com.sogo.ee.midd.service;

import java.util.List;
import java.util.Optional;

import com.sogo.ee.midd.model.entity.EmployeeApprovalAmount;

/**
 * 員工審批金額服務介面
 * 定義操作 EmployeeApprovalAmount 的方法
 */
public interface EmployeeApprovalAmountService {

    /**
     * 獲取所有員工審批金額信息
     *
     * @return 員工審批金額信息列表
     */
    List<EmployeeApprovalAmount> findAll();
    
    /**
     * 根據員工編號查詢審批金額信息
     *
     * @param employeeNo 員工編號
     * @return 員工審批金額信息
     */
    Optional<EmployeeApprovalAmount> findByEmployeeNo(String employeeNo);
} 