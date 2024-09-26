package com.sogo.ee.midd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIOrganizationManager;

@Repository
public interface APIOrganizationManagerRepository extends JpaRepository<APIOrganizationManager, Long> {

    @Modifying
    @Query(value = "TRUNCATE TABLE api_organization_manager", nativeQuery = true)
    void truncateTable();

    boolean existsByEmployeeNoAndOrgCode(String employeeNo, String orgCode);

}
