package com.sogo.ee.midd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sogo.ee.midd.model.entity.APIOrganizationRelationArchived;

public interface APIOrganizationRelationArchivedRepository extends JpaRepository<APIOrganizationRelationArchived, Long> {

    @Modifying
    @Query(value = "TRUNCATE TABLE api_organization_relation_archived", nativeQuery = true)
    void truncateTable();

}
