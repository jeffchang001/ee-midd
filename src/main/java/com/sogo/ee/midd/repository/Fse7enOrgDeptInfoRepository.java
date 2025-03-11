package com.sogo.ee.midd.repository;

import com.sogo.ee.midd.model.entity.Fse7enOrgDeptInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 部門資訊資料庫操作介面
 */
@Repository
public interface Fse7enOrgDeptInfoRepository extends JpaRepository<Fse7enOrgDeptInfo, String> {
} 