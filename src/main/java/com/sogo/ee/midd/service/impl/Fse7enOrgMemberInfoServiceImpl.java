package com.sogo.ee.midd.service.impl;

import com.sogo.ee.midd.model.entity.Fse7enOrgMemberInfo;
import com.sogo.ee.midd.repository.Fse7enOrgMemberInfoRepository;
import com.sogo.ee.midd.service.Fse7enOrgMemberInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 成員資訊服務實現類
 */
@Service
public class Fse7enOrgMemberInfoServiceImpl implements Fse7enOrgMemberInfoService {

    @Autowired
    private Fse7enOrgMemberInfoRepository fse7enOrgMemberInfoRepository;

    /**
     * 查詢所有成員資訊
     * @return 成員資訊列表
     */
    @Override
    public List<Fse7enOrgMemberInfo> findAll() {
        return fse7enOrgMemberInfoRepository.findAll();
    }

    /**
     * 根據員工編號查詢成員資訊
     * @param employeeNo 員工編號
     * @return 成員資訊
     */
    @Override
    public Optional<Fse7enOrgMemberInfo> findByEmployeeNo(String employeeNo) {
        return fse7enOrgMemberInfoRepository.findById(employeeNo);
    }

    /**
     * 根據電子郵件地址查詢成員資訊
     * @param emailAddress 電子郵件地址
     * @return 成員資訊
     */
    @Override
    public Optional<Fse7enOrgMemberInfo> findByEmailAddress(String emailAddress) {
        return fse7enOrgMemberInfoRepository.findAll().stream()
                .filter(member -> emailAddress.equals(member.getEmailAddress()))
                .findFirst();
    }

    /**
     * 根據 Azure 帳號查詢成員資訊
     * @param azureAccount Azure 帳號
     * @return 成員資訊
     */
    @Override
    public Optional<Fse7enOrgMemberInfo> findByAzureAccount(String azureAccount) {
        return fse7enOrgMemberInfoRepository.findAll().stream()
                .filter(member -> azureAccount.equals(member.getAzureAccount()))
                .findFirst();
    }

    /**
     * 查詢在職成員資訊
     * @return 在職成員資訊列表
     */
    @Override
    public List<Fse7enOrgMemberInfo> findByIsTerminated(String isTerminated) {
        return fse7enOrgMemberInfoRepository.findAll().stream()
                .filter(member -> isTerminated.equals(member.getIsTerminated()))
                .collect(Collectors.toList());
    }
} 