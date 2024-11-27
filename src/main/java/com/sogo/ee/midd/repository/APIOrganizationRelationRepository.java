package com.sogo.ee.midd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIOrganizationRelation;
@Repository
public interface APIOrganizationRelationRepository extends JpaRepository<APIOrganizationRelation, Long> {

    
    @Modifying
    @Query(value = "TRUNCATE TABLE api_organization_relation", nativeQuery = true)
    void truncateTable();

    @Query("SELECT r FROM APIOrganizationRelation r " +
           "JOIN APIOrganization o ON r.orgCode = o.orgCode " +
           "WHERE r.status = :status")
    List<APIOrganizationRelation> findByStatus(String status);

    @Query("SELECT r FROM APIOrganizationRelation r " +
           "JOIN APIOrganization o ON r.orgCode = o.orgCode " +
           "WHERE r.orgTreeType = :orgTreeType")
    List<APIOrganizationRelation> findByOrgTreeType(String orgTreeType);

}
