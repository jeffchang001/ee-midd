package com.sogo.ee.midd.service;

import com.sogo.ee.midd.model.entity.Fse7enOrgMemberStruct;
import com.sogo.ee.midd.model.entity.Fse7enOrgMemberStructId;

import java.util.List;
import java.util.Optional;

/**
 * 成員結構資訊服務介面
 */
public interface Fse7enOrgMemberStructService {

    /**
     * 查詢所有成員結構資訊
     * @return 成員結構資訊列表
     */
    List<Fse7enOrgMemberStruct> findAll();

    /**
     * 根據複合主鍵查詢成員結構資訊
     * @param id 複合主鍵
     * @return 成員結構資訊
     */
    Optional<Fse7enOrgMemberStruct> findById(Fse7enOrgMemberStructId id);

    /**
     * 根據員工編號查詢成員結構資訊
     * @param employeeNo 員工編號
     * @return 成員結構資訊列表
     */
    List<Fse7enOrgMemberStruct> findByEmployeeNo(String employeeNo);

    /**
     * 根據組織代碼查詢成員結構資訊
     * @param orgCode 組織代碼
     * @return 成員結構資訊列表
     */
    List<Fse7enOrgMemberStruct> findByOrgCode(String orgCode);

    /**
     * 根據職稱代碼查詢成員結構資訊
     * @param jobTitleCode 職稱代碼
     * @return 成員結構資訊列表
     */
    List<Fse7enOrgMemberStruct> findByJobTitleCode(String jobTitleCode);

    /**
     * 根據職稱等級查詢成員結構資訊
     * @param jobGrade 職稱等級
     * @return 成員結構資訊列表
     */
    List<Fse7enOrgMemberStruct> findByJobGrade(String jobGrade);

    /**
     * 查詢主要職位的成員結構資訊
     * @return 主要職位的成員結構資訊列表
     */
    List<Fse7enOrgMemberStruct> findByIsMainJob(String isMainJob);

    /**
     * 查詢有審批權限的成員結構資訊
     * @return 有審批權限的成員結構資訊列表
     */
    List<Fse7enOrgMemberStruct> findByApproveRight(String approveRight);

    /**
     * 查詢啟用的成員結構資訊
     * @return 啟用的成員結構資訊列表
     */
    List<Fse7enOrgMemberStruct> findByEnable(String enable);
} 