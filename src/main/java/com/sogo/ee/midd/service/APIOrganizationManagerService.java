package com.sogo.ee.midd.service;

import org.springframework.http.ResponseEntity;

public interface APIOrganizationManagerService {

    public void initOrganizationManager(ResponseEntity<String> response) throws Exception;

    public void processOrganizationManager(ResponseEntity<String> response) throws Exception;

}
