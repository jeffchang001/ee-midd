package com.sogo.ee.midd.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sogo.ee.midd.model.entity.APIOrganizationActionLog;

public interface APIOrganizationActionLogRepository extends JpaRepository<APIOrganizationActionLog, Long> {

    List<APIOrganizationActionLog> findByOrgCodeAndActionAndCreatedDate(String orgCode, String action,
            LocalDateTime createdDate);

    List<APIOrganizationActionLog> findByAction(String action);

    @Query("SELECT log FROM APIOrganizationActionLog log " +
            "WHERE function('to_char', log.createdDate, 'YYYY-MM-DD') = :date")
    List<APIOrganizationActionLog> findByCreatedDate(@Param("date") String createdDate);

}