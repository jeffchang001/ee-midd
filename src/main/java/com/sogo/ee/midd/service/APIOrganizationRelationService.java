package com.sogo.ee.midd.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sogo.ee.midd.model.dto.WholeOrgTreeDto;

public interface APIOrganizationRelationService {

    public void processOrganizationRelation(ResponseEntity<String> response) throws Exception;

    public List<WholeOrgTreeDto> fetchOrganizationRelationByorgTreeType(String orgTreeType) throws Exception;
    
}
