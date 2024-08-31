package com.sogo.ee.midd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APIOrganizationRelation;
@Repository
public interface IAPIOrganizationRelationRepository extends JpaRepository<APIOrganizationRelation, Long> {

    
    @Modifying
    @Query(value = "TRUNCATE TABLE api_organization_relation", nativeQuery = true)
    void truncateTable();

    List<APIOrganizationRelation> findByStatus(String status);
}
