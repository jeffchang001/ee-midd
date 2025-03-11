package com.sogo.ee.midd.service.impl;

import com.sogo.ee.midd.model.entity.Fse7enOrgDeptGradeInfo;
import com.sogo.ee.midd.repository.Fse7enOrgDeptGradeInfoRepository;
import com.sogo.ee.midd.service.Fse7enOrgDeptGradeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 部門等級資訊服務實現類
 */
@Service
public class Fse7enOrgDeptGradeInfoServiceImpl implements Fse7enOrgDeptGradeInfoService {

    @Autowired
    private Fse7enOrgDeptGradeInfoRepository fse7enOrgDeptGradeInfoRepository;

    /**
     * 查詢所有部門等級資訊
     * @return 部門等級資訊列表
     */
    @Override
    public List<Fse7enOrgDeptGradeInfo> findAll() {
        return fse7enOrgDeptGradeInfoRepository.findAll();
    }

    /**
     * 根據等級ID查詢部門等級資訊
     * @param gradeId 等級ID
     * @return 部門等級資訊
     */
    @Override
    public Optional<Fse7enOrgDeptGradeInfo> findByGradeId(String gradeId) {
        return fse7enOrgDeptGradeInfoRepository.findById(gradeId);
    }
} 