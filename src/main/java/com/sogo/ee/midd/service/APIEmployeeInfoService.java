package com.sogo.ee.midd.service;

import org.springframework.http.ResponseEntity;

public interface APIEmployeeInfoService {

    public void initEmployeeInfo(ResponseEntity<String> response) throws Exception;

    public void processEmployeeInfo(ResponseEntity<String> response) throws Exception;

    public void compareAndProcessEmployeeInfo(ResponseEntity<String> response) throws Exception;

}
