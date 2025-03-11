package com.sogo.ee.midd.service.impl;

import com.sogo.ee.midd.model.entity.Fse7enOrgMemberStruct;
import com.sogo.ee.midd.model.entity.Fse7enOrgMemberStructId;
import com.sogo.ee.midd.repository.Fse7enOrgMemberStructRepository;
import com.sogo.ee.midd.service.Fse7enOrgMemberStructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 成員結構資訊服務實現類
 */
@Service
public class Fse7enOrgMemberStructServiceImpl implements Fse7enOrgMemberStructService {

    @Autowired
    private Fse7enOrgMemberStructRepository fse7enOrgMemberStructRepository;

    /**
     * 查詢所有成員結構資訊
     * @return 成員結構資訊列表
     */
    @Override
    public List<Fse7enOrgMemberStruct> findAll() {
        return fse7enOrgMemberStructRepository.findAll();
    }

    /**
     * 根據複合主鍵查詢成員結構資訊
     * @param id 複合主鍵
     * @return 成員結構資訊
     */
    @Override
    public Optional<Fse7enOrgMemberStruct> findById(Fse7enOrgMemberStructId id) {
        return fse7enOrgMemberStructRepository.findById(id);
    }

    /**
     * 根據員工編號查詢成員結構資訊
     * @param employeeNo 員工編號
     * @return 成員結構資訊列表
     */
    @Override
    public List<Fse7enOrgMemberStruct> findByEmployeeNo(String employeeNo) {
        return fse7enOrgMemberStructRepository.findAll().stream()
                .filter(struct -> employeeNo.equals(struct.getEmployeeNo()))
                .collect(Collectors.toList());
    }

    /**
     * 根據組織代碼查詢成員結構資訊
     * @param orgCode 組織代碼
     * @return 成員結構資訊列表
     */
    @Override
    public List<Fse7enOrgMemberStruct> findByOrgCode(String orgCode) {
        return fse7enOrgMemberStructRepository.findAll().stream()
                .filter(struct -> orgCode.equals(struct.getOrgCode()))
                .collect(Collectors.toList());
    }

    /**
     * 根據職稱代碼查詢成員結構資訊
     * @param jobTitleCode 職稱代碼
     * @return 成員結構資訊列表
     */
    @Override
    public List<Fse7enOrgMemberStruct> findByJobTitleCode(String jobTitleCode) {
        return fse7enOrgMemberStructRepository.findAll().stream()
                .filter(struct -> jobTitleCode.equals(struct.getJobTitleCode()))
                .collect(Collectors.toList());
    }

    /**
     * 根據職稱等級查詢成員結構資訊
     * @param jobGrade 職稱等級
     * @return 成員結構資訊列表
     */
    @Override
    public List<Fse7enOrgMemberStruct> findByJobGrade(String jobGrade) {
        return fse7enOrgMemberStructRepository.findAll().stream()
                .filter(struct -> jobGrade.equals(struct.getJobGrade()))
                .collect(Collectors.toList());
    }

    /**
     * 查詢主要職位的成員結構資訊
     * @return 主要職位的成員結構資訊列表
     */
    @Override
    public List<Fse7enOrgMemberStruct> findByIsMainJob(String isMainJob) {
        return fse7enOrgMemberStructRepository.findAll().stream()
                .filter(struct -> isMainJob.equals(struct.getIsMainJob()))
                .collect(Collectors.toList());
    }

    /**
     * 查詢有審批權限的成員結構資訊
     * @return 有審批權限的成員結構資訊列表
     */
    @Override
    public List<Fse7enOrgMemberStruct> findByApproveRight(String approveRight) {
        return fse7enOrgMemberStructRepository.findAll().stream()
                .filter(struct -> approveRight.equals(struct.getApproveRight()))
                .collect(Collectors.toList());
    }

    /**
     * 查詢啟用的成員結構資訊
     * @return 啟用的成員結構資訊列表
     */
    @Override
    public List<Fse7enOrgMemberStruct> findByEnable(String enable) {
        return fse7enOrgMemberStructRepository.findAll().stream()
                .filter(struct -> enable.equals(struct.getEnable()))
                .collect(Collectors.toList());
    }
} 