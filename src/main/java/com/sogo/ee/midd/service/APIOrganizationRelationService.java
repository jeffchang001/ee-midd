package com.sogo.ee.midd.service;

import org.springframework.http.ResponseEntity;

public interface APIOrganizationRelationService {

    public void processOrganizationRelation(ResponseEntity<String> response) throws Exception;
    
}
