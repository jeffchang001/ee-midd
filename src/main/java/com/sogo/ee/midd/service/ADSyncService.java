package com.sogo.ee.midd.service;

import java.util.List;

import com.sogo.ee.midd.model.dto.ADSyncDto;


public interface ADSyncService {
    public List<ADSyncDto> getADSyncData() throws Exception;
} 