package com.sogo.ee.midd.service.impl;

import com.sogo.ee.midd.model.entity.CoreEhrEmployee;
import com.sogo.ee.midd.repository.CoreEhrEmployeeRepository;
import com.sogo.ee.midd.service.CoreEhrEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 核心 EHR 員工服務實現類
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CoreEhrEmployeeServiceImpl implements CoreEhrEmployeeService {

    private final CoreEhrEmployeeRepository coreEhrEmployeeRepository;

    /**
     * 查詢所有員工資訊
     * @return 員工資訊列表
     */
    @Override
    public List<CoreEhrEmployee> findAll() {
        log.debug("查詢所有員工資訊");
        return coreEhrEmployeeRepository.findAll();
    }

    /**
     * 根據員工編號查詢員工資訊
     * @param employeeNo 員工編號
     * @return 員工資訊
     */
    @Override
    public Optional<CoreEhrEmployee> findByEmployeeNo(String employeeNo) {
        log.debug("根據員工編號查詢員工資訊: {}", employeeNo);
        return coreEhrEmployeeRepository.findById(employeeNo);
    }

    /**
     * 根據姓名查詢員工
     * @param fullName 姓名
     * @return 員工列表
     */
    @Override
    public List<CoreEhrEmployee> findByFullNameContaining(String fullName) {
        log.debug("根據姓名查詢員工: {}", fullName);
        return coreEhrEmployeeRepository.findByFullNameContaining(fullName);
    }

    /**
     * 根據電子郵件地址查詢員工
     * @param emailAddress 電子郵件地址
     * @return 員工
     */
    @Override
    public Optional<CoreEhrEmployee> findByEmailAddress(String emailAddress) {
        log.debug("根據電子郵件地址查詢員工: {}", emailAddress);
        return coreEhrEmployeeRepository.findByEmailAddress(emailAddress);
    }

    /**
     * 根據部門代碼查詢員工
     * @param formulaOrgCode 部門代碼
     * @return 員工列表
     */
    @Override
    public List<CoreEhrEmployee> findByFormulaOrgCode(String formulaOrgCode) {
        log.debug("根據部門代碼查詢員工: {}", formulaOrgCode);
        return coreEhrEmployeeRepository.findByFormulaOrgCode(formulaOrgCode);
    }

    /**
     * 根據職稱代碼查詢員工
     * @param jobTitleCode 職稱代碼
     * @return 員工列表
     */
    @Override
    public List<CoreEhrEmployee> findByJobTitleCode(String jobTitleCode) {
        log.debug("根據職稱代碼查詢員工: {}", jobTitleCode);
        return coreEhrEmployeeRepository.findByJobTitleCode(jobTitleCode);
    }

    /**
     * 根據職級代碼查詢員工
     * @param jobGradeCode 職級代碼
     * @return 員工列表
     */
    @Override
    public List<CoreEhrEmployee> findByJobGradeCode(String jobGradeCode) {
        log.debug("根據職級代碼查詢員工: {}", jobGradeCode);
        return coreEhrEmployeeRepository.findByJobGradeCode(jobGradeCode);
    }

    /**
     * 根據在職狀態查詢員工
     * @param employedStatus 在職狀態
     * @return 員工列表
     */
    @Override
    public List<CoreEhrEmployee> findByEmployedStatus(String employedStatus) {
        log.debug("根據在職狀態查詢員工: {}", employedStatus);
        return coreEhrEmployeeRepository.findByEmployedStatus(employedStatus);
    }

    /**
     * 根據主管員工編號查詢下屬員工
     * @param managerEmployeeNo 主管員工編號
     * @return 下屬員工列表
     */
    @Override
    public List<CoreEhrEmployee> findByManagerEmployeeNo(String managerEmployeeNo) {
        log.debug("根據主管員工編號查詢下屬員工: {}", managerEmployeeNo);
        return coreEhrEmployeeRepository.findByManagerEmployeeNo(managerEmployeeNo);
    }
} 