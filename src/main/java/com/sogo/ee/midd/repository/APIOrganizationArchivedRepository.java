package com.sogo.ee.midd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sogo.ee.midd.model.entity.APIOrganizationArchived;

public interface APIOrganizationArchivedRepository extends JpaRepository<APIOrganizationArchived, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE api_organization_archived", nativeQuery = true)
    void truncateTable();

    APIOrganizationArchived findByOrgCode(String orgCode);
}