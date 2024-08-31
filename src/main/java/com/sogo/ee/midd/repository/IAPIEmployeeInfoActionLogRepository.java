package com.sogo.ee.midd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIEmployeeInfoActionLog;

@Repository
public interface IAPIEmployeeInfoActionLogRepository extends JpaRepository<APIEmployeeInfoActionLog, Long> {

}
