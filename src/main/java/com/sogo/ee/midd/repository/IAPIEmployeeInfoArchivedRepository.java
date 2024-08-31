package com.sogo.ee.midd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIEmployeeInfoArchived;

@Repository
public interface IAPIEmployeeInfoArchivedRepository extends JpaRepository<APIEmployeeInfoArchived, Long> {

    @Modifying
    @Query(value = "TRUNCATE TABLE api_employee_info_archived", nativeQuery = true)
    void truncateTable();

    // 自動生成的方法，查詢 employedStatus = 1 的記錄
    List<APIEmployeeInfoArchived> findByEmployedStatus(String employedStatus);

    // 新增的查詢方法：查詢 employedStatus = '1' 且 status = 'C' 的記錄
    List<APIEmployeeInfoArchived> findByEmployedStatusAndStatus(String employedStatus, String status);
    
    APIEmployeeInfoArchived findByEmployeeNo(String employeeNo);

}
