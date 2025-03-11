package com.sogo.ee.midd.repository;

import com.sogo.ee.midd.model.entity.Fse7enOrgDeptStruct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 部門結構資訊資料庫操作介面
 */
@Repository
public interface Fse7enOrgDeptStructRepository extends JpaRepository<Fse7enOrgDeptStruct, String> {
} 