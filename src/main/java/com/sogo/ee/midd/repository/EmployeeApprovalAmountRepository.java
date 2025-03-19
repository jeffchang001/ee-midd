package com.sogo.ee.midd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.EmployeeApprovalAmount;

/**
 * 員工審批金額儲存庫
 * 提供對 view_employee_approval_amount 視圖的數據訪問
 */
@Repository
public interface EmployeeApprovalAmountRepository extends JpaRepository<EmployeeApprovalAmount, String> {
    // 可以根據需要添加自定義查詢方法
} 