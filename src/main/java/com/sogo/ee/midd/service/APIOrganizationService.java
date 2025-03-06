package com.sogo.ee.midd.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface APIOrganizationService {

    public void initOrganization(ResponseEntity<String> response) throws Exception;

    public void processOrganization(ResponseEntity<String> response) throws Exception;

    public void compareAndProcessOrganization(ResponseEntity<String> response) throws Exception;

    public List<String> getEmptyOrganizationDNs(String orgTreeType) throws Exception;

}
