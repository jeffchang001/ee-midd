package com.sogo.ee.midd.service;

import org.springframework.http.ResponseEntity;

public interface APIOrganizationService {

    public void initOrganization(ResponseEntity<String> response) throws Exception;

    public void processOrganization(ResponseEntity<String> response) throws Exception;

    public void compareAndProcessOrganization(ResponseEntity<String> response) throws Exception;

}
