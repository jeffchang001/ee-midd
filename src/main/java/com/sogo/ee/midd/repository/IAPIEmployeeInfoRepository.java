package com.sogo.ee.midd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIEmployeeInfo;

@Repository
public interface IAPIEmployeeInfoRepository extends JpaRepository<APIEmployeeInfo, Long> {
    
    @Modifying
    @Query(value = "TRUNCATE TABLE api_employee_info", nativeQuery = true)
    void truncateTable();

    // 自動生成的方法，查詢 employedStatus = 1 的記錄
    List<APIEmployeeInfo> findByEmployedStatus(String employedStatus);
}
