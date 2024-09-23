package com.sogo.ee.midd.service;

import org.springframework.http.ResponseEntity;

public interface APICompanyService {

    public void initCompany(ResponseEntity<String> response) throws Exception;

    public void processCompany(ResponseEntity<String> response) throws Exception;

}
