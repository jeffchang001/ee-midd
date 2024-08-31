package com.sogo.ee.midd.controller;

import com.sogo.ee.midd.dto.ADSyncDto;
import com.sogo.ee.midd.service.ADSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ADSyncController {

    @Autowired
    private ADSyncService adSyncService;

    @GetMapping("/getADSyncData")
    public ResponseEntity<ADSyncDto> getADSyncData() {
        // ADSyncDto adSyncDto = adSyncService.getADSyncData();
        // return ResponseEntity.ok(adSyncDto);
        return null;
    }
}