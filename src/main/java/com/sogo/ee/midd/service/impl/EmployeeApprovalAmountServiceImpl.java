package com.sogo.ee.midd.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sogo.ee.midd.model.entity.EmployeeApprovalAmount;
import com.sogo.ee.midd.repository.EmployeeApprovalAmountRepository;
import com.sogo.ee.midd.service.EmployeeApprovalAmountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 員工審批金額服務實作
 * 實作 EmployeeApprovalAmountService 介面中定義的方法
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeApprovalAmountServiceImpl implements EmployeeApprovalAmountService {

    private final EmployeeApprovalAmountRepository employeeApprovalAmountRepository;

    /**
     * 獲取所有員工審批金額信息
     *
     * @return 員工審批金額信息列表
     */
    @Override
    public List<EmployeeApprovalAmount> findAll() {
        log.info("獲取所有員工審批金額信息");
        return employeeApprovalAmountRepository.findAll();
    }

    /**
     * 根據員工編號查詢審批金額信息
     *
     * @param employeeNo 員工編號
     * @return 員工審批金額信息
     */
    @Override
    public Optional<EmployeeApprovalAmount> findByEmployeeNo(String employeeNo) {
        log.info("根據員工編號查詢審批金額信息: {}", employeeNo);
        return employeeApprovalAmountRepository.findById(employeeNo);
    }
} 