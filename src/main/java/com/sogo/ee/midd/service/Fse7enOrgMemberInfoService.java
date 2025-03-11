package com.sogo.ee.midd.service;

import com.sogo.ee.midd.model.entity.Fse7enOrgMemberInfo;

import java.util.List;
import java.util.Optional;

/**
 * 成員資訊服務介面
 */
public interface Fse7enOrgMemberInfoService {

    /**
     * 查詢所有成員資訊
     * @return 成員資訊列表
     */
    List<Fse7enOrgMemberInfo> findAll();

    /**
     * 根據員工編號查詢成員資訊
     * @param employeeNo 員工編號
     * @return 成員資訊
     */
    Optional<Fse7enOrgMemberInfo> findByEmployeeNo(String employeeNo);

    /**
     * 根據電子郵件地址查詢成員資訊
     * @param emailAddress 電子郵件地址
     * @return 成員資訊
     */
    Optional<Fse7enOrgMemberInfo> findByEmailAddress(String emailAddress);

    /**
     * 根據 Azure 帳號查詢成員資訊
     * @param azureAccount Azure 帳號
     * @return 成員資訊
     */
    Optional<Fse7enOrgMemberInfo> findByAzureAccount(String azureAccount);

    /**
     * 查詢在職成員資訊
     * @return 在職成員資訊列表
     */
    List<Fse7enOrgMemberInfo> findByIsTerminated(String isTerminated);
} 