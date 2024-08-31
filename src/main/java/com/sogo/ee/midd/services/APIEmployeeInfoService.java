package com.sogo.ee.midd.services;

import org.springframework.http.ResponseEntity;

public interface APIEmployeeInfoService {

    public void processEmployeeInfo(ResponseEntity<String> response) throws Exception;

}
