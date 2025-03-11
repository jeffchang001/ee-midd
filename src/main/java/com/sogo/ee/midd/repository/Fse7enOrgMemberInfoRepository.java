package com.sogo.ee.midd.repository;

import com.sogo.ee.midd.model.entity.Fse7enOrgMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 成員資訊資料庫操作介面
 */
@Repository
public interface Fse7enOrgMemberInfoRepository extends JpaRepository<Fse7enOrgMemberInfo, String> {
} 