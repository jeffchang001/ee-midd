package com.sogo.ee.midd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIEmployeeInfoArchived;

@Repository
public interface IAPIEmployeeInfoArchivedRepository extends JpaRepository<APIEmployeeInfoArchived, Long> {

    // 自動生成的方法，查詢 employedStatus = 1 的記錄
    List<APIEmployeeInfoArchived> findByEmployedStatus(String employedStatus);

}
