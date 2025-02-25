package com.sogo.ee.midd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIEmployeeInfoActionLog;


@Repository
public interface ADSyncRepository extends JpaRepository<APIEmployeeInfoActionLog, Long> {

    @Query("SELECT log FROM APIEmployeeInfoActionLog log " +
       "WHERE function('to_char', log.createdDate, 'YYYY-MM-DD') = :date")
    List<APIEmployeeInfoActionLog> findByCreatedDate(@Param("date")String createdDate);
}
