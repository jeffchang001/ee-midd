package com.sogo.ee.midd.repository;

import com.sogo.ee.midd.model.entity.Fse7enOrgJobTitle2Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 職稱對應等級資訊資料庫操作介面
 */
@Repository
public interface Fse7enOrgJobTitle2GradeRepository extends JpaRepository<Fse7enOrgJobTitle2Grade, String> {
} 