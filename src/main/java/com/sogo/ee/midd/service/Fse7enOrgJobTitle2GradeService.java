package com.sogo.ee.midd.service;

import com.sogo.ee.midd.model.entity.Fse7enOrgJobTitle2Grade;

import java.util.List;
import java.util.Optional;

/**
 * 職稱對應等級資訊服務介面
 */
public interface Fse7enOrgJobTitle2GradeService {

    /**
     * 查詢所有職稱對應等級資訊
     * @return 職稱對應等級資訊列表
     */
    List<Fse7enOrgJobTitle2Grade> findAll();

    /**
     * 根據職稱代碼查詢職稱對應等級資訊
     * @param jobTitleCode 職稱代碼
     * @return 職稱對應等級資訊
     */
    Optional<Fse7enOrgJobTitle2Grade> findByJobTitleCode(String jobTitleCode);

    /**
     * 根據職稱等級查詢職稱對應等級資訊
     * @param jobGrade 職稱等級
     * @return 職稱對應等級資訊列表
     */
    List<Fse7enOrgJobTitle2Grade> findByJobGrade(String jobGrade);
} 