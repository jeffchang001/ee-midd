package com.sogo.ee.midd.service.impl;

import com.sogo.ee.midd.model.entity.CoreEhrDepartment;
import com.sogo.ee.midd.repository.CoreEhrDepartmentRepository;
import com.sogo.ee.midd.service.CoreEhrDepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 核心 EHR 部門服務實現類
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CoreEhrDepartmentServiceImpl implements CoreEhrDepartmentService {

    private final CoreEhrDepartmentRepository coreEhrDepartmentRepository;

    /**
     * 查詢所有部門資訊
     * @return 部門資訊列表
     */
    @Override
    public List<CoreEhrDepartment> findAll() {
        log.debug("查詢所有部門資訊");
        return coreEhrDepartmentRepository.findAll();
    }

    /**
     * 根據部門代碼查詢部門資訊
     * @param orgCode 部門代碼
     * @return 部門資訊
     */
    @Override
    public Optional<CoreEhrDepartment> findByOrgCode(String orgCode) {
        log.debug("根據部門代碼查詢部門資訊: {}", orgCode);
        return coreEhrDepartmentRepository.findById(orgCode);
    }

    /**
     * 根據部門名稱查詢部門
     * @param orgName 部門名稱
     * @return 部門列表
     */
    @Override
    public List<CoreEhrDepartment> findByOrgNameContaining(String orgName) {
        log.debug("根據部門名稱查詢部門: {}", orgName);
        return coreEhrDepartmentRepository.findByOrgNameContaining(orgName);
    }

    /**
     * 根據部門簡稱查詢部門
     * @param orgAbbrName 部門簡稱
     * @return 部門列表
     */
    @Override
    public List<CoreEhrDepartment> findByOrgAbbrNameContaining(String orgAbbrName) {
        log.debug("根據部門簡稱查詢部門: {}", orgAbbrName);
        return coreEhrDepartmentRepository.findByOrgAbbrNameContaining(orgAbbrName);
    }

    /**
     * 根據上級部門代碼查詢子部門
     * @param parentOrgCode 上級部門代碼
     * @return 子部門列表
     */
    @Override
    public List<CoreEhrDepartment> findByParentOrgCode(String parentOrgCode) {
        log.debug("根據上級部門代碼查詢子部門: {}", parentOrgCode);
        return coreEhrDepartmentRepository.findByParentOrgCode(parentOrgCode);
    }

    /**
     * 根據主管員工編號查詢部門
     * @param managerEmployeeNo 主管員工編號
     * @return 部門
     */
    @Override
    public Optional<CoreEhrDepartment> findByManagerEmployeeNo(String managerEmployeeNo) {
        log.debug("根據主管員工編號查詢部門: {}", managerEmployeeNo);
        return coreEhrDepartmentRepository.findByManagerEmployeeNo(managerEmployeeNo);
    }

    /**
     * 查詢非虛擬部門
     * @param isVirtual 是否虛擬部門
     * @return 部門列表
     */
    @Override
    public List<CoreEhrDepartment> findByIsVirtual(Boolean isVirtual) {
        log.debug("查詢虛擬部門狀態為: {}", isVirtual);
        return coreEhrDepartmentRepository.findByIsVirtual(isVirtual);
    }
} 