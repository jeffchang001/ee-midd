package com.sogo.ee.midd.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIEmployeeInfoActionLog;

@Repository
public interface APIEmployeeInfoActionLogRepository extends JpaRepository<APIEmployeeInfoActionLog, Long> {

    List<APIEmployeeInfoActionLog> findByEmployeeNoAndAction(String employeeNo, String action);

    List<APIEmployeeInfoActionLog> findByEmployeeNoAndActionAndCreatedDate(String employeeNo, String action, LocalDateTime createdDate);

    List<APIEmployeeInfoActionLog> findByAction(String action);

}

