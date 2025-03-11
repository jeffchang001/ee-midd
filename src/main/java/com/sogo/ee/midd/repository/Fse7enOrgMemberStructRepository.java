package com.sogo.ee.midd.repository;

import com.sogo.ee.midd.model.entity.Fse7enOrgMemberStruct;
import com.sogo.ee.midd.model.entity.Fse7enOrgMemberStructId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 成員結構資訊資料庫操作介面
 */
@Repository
public interface Fse7enOrgMemberStructRepository extends JpaRepository<Fse7enOrgMemberStruct, Fse7enOrgMemberStructId> {
} 