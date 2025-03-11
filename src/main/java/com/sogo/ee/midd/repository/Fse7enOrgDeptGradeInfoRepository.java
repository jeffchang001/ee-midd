package com.sogo.ee.midd.repository;

import com.sogo.ee.midd.model.entity.Fse7enOrgDeptGradeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 部門等級資訊資料庫操作介面
 */
@Repository
public interface Fse7enOrgDeptGradeInfoRepository extends JpaRepository<Fse7enOrgDeptGradeInfo, String> {
} 