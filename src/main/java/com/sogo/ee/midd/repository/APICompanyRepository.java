package com.sogo.ee.midd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sogo.ee.midd.model.entity.APICompany;

@Repository
public interface APICompanyRepository extends JpaRepository<APICompany, Long> {

    @Modifying
    @Query(value = "TRUNCATE TABLE api_company", nativeQuery = true)
    void truncateTable();

}
