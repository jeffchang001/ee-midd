package com.sogo.ee.midd.service.impl;

import com.sogo.ee.midd.model.entity.Fse7enOrgDeptStruct;
import com.sogo.ee.midd.repository.Fse7enOrgDeptStructRepository;
import com.sogo.ee.midd.service.Fse7enOrgDeptStructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 部門結構資訊服務實現類
 */
@Service
public class Fse7enOrgDeptStructServiceImpl implements Fse7enOrgDeptStructService {

    @Autowired
    private Fse7enOrgDeptStructRepository fse7enOrgDeptStructRepository;

    /**
     * 查詢所有部門結構資訊
     * @return 部門結構資訊列表
     */
    @Override
    public List<Fse7enOrgDeptStruct> findAll() {
        return fse7enOrgDeptStructRepository.findAll();
    }

    /**
     * 根據組織代碼查詢部門結構資訊
     * @param orgCode 組織代碼
     * @return 部門結構資訊
     */
    @Override
    public Optional<Fse7enOrgDeptStruct> findByOrgCode(String orgCode) {
        return fse7enOrgDeptStructRepository.findById(orgCode);
    }

    /**
     * 根據父組織代碼查詢部門結構資訊
     * @param parentOrgCode 父組織代碼
     * @return 部門結構資訊列表
     */
    @Override
    public List<Fse7enOrgDeptStruct> findByParentOrgCode(String parentOrgCode) {
        return fse7enOrgDeptStructRepository.findAll().stream()
                .filter(struct -> parentOrgCode.equals(struct.getParentOrgCode()))
                .collect(Collectors.toList());
    }

    /**
     * 根據頂層組織代碼查詢部門結構資訊
     * @param topOrgtreeCode 頂層組織代碼
     * @return 部門結構資訊列表
     */
    @Override
    public List<Fse7enOrgDeptStruct> findByTopOrgtreeCode(String topOrgtreeCode) {
        return fse7enOrgDeptStructRepository.findAll().stream()
                .filter(struct -> topOrgtreeCode.equals(struct.getTopOrgtreeCode()))
                .collect(Collectors.toList());
    }
} 