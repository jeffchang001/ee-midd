package com.sogo.ee.midd.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogo.ee.midd.model.entity.APIOrganizationActionLog;

public interface APIOrganizationActionLogRepository extends JpaRepository<APIOrganizationActionLog, Long> {

    List<APIOrganizationActionLog> findByOrgCodeAndActionAndCreatedDate(String orgCode, String action, LocalDateTime createdDate);

}