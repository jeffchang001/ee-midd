package com.sogo.ee.midd.service.impl;

import com.sogo.ee.midd.model.entity.Fse7enOrgJobTitle2Grade;
import com.sogo.ee.midd.repository.Fse7enOrgJobTitle2GradeRepository;
import com.sogo.ee.midd.service.Fse7enOrgJobTitle2GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 職稱對應等級資訊服務實現類
 */
@Service
public class Fse7enOrgJobTitle2GradeServiceImpl implements Fse7enOrgJobTitle2GradeService {

    @Autowired
    private Fse7enOrgJobTitle2GradeRepository fse7enOrgJobTitle2GradeRepository;

    /**
     * 查詢所有職稱對應等級資訊
     * @return 職稱對應等級資訊列表
     */
    @Override
    public List<Fse7enOrgJobTitle2Grade> findAll() {
        return fse7enOrgJobTitle2GradeRepository.findAll();
    }

    /**
     * 根據職稱代碼查詢職稱對應等級資訊
     * @param jobTitleCode 職稱代碼
     * @return 職稱對應等級資訊
     */
    @Override
    public Optional<Fse7enOrgJobTitle2Grade> findByJobTitleCode(String jobTitleCode) {
        return fse7enOrgJobTitle2GradeRepository.findById(jobTitleCode);
    }

    /**
     * 根據職稱等級查詢職稱對應等級資訊
     * @param jobGrade 職稱等級
     * @return 職稱對應等級資訊列表
     */
    @Override
    public List<Fse7enOrgJobTitle2Grade> findByJobGrade(String jobGrade) {
        return fse7enOrgJobTitle2GradeRepository.findAll().stream()
                .filter(grade -> jobGrade.equals(grade.getJobGrade()))
                .collect(Collectors.toList());
    }
} 