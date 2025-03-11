package com.sogo.ee.midd.service.impl;

import com.sogo.ee.midd.model.entity.Fse7enOrgDeptInfo;
import com.sogo.ee.midd.repository.Fse7enOrgDeptInfoRepository;
import com.sogo.ee.midd.service.Fse7enOrgDeptInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 部門資訊服務實現類
 */
@Service
public class Fse7enOrgDeptInfoServiceImpl implements Fse7enOrgDeptInfoService {

    @Autowired
    private Fse7enOrgDeptInfoRepository fse7enOrgDeptInfoRepository;

    /**
     * 查詢所有部門資訊
     * @return 部門資訊列表
     */
    @Override
    public List<Fse7enOrgDeptInfo> findAll() {
        return fse7enOrgDeptInfoRepository.findAll();
    }

    /**
     * 根據組織代碼查詢部門資訊
     * @param orgCode 組織代碼
     * @return 部門資訊
     */
    @Override
    public Optional<Fse7enOrgDeptInfo> findByOrgCode(String orgCode) {
        return fse7enOrgDeptInfoRepository.findById(orgCode);
    }
} 