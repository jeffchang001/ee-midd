package com.sogo.ee.midd.services;

import org.springframework.http.ResponseEntity;

public interface APIOrganizationRelationService {

    public void processOrganizationRelation(ResponseEntity<String> response) throws Exception;
    
}
