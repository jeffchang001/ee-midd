package com.sogo.ee.midd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIOrganization;

@Repository
public interface APIOrganizationRepository extends JpaRepository<APIOrganization, Long> {

    @Modifying
    @Query(value = "TRUNCATE TABLE api_organization", nativeQuery = true)
    void truncateTable();

    List<APIOrganization> findByStatus(String status);

    APIOrganization findByOrgCode(String orgCode);

}
