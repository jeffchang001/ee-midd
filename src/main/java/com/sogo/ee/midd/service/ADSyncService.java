package com.sogo.ee.midd.service;

import java.time.LocalDate;
import java.util.List;

import com.sogo.ee.midd.model.dto.ADSyncDto;

public interface ADSyncService {
    public List<ADSyncDto> getADSyncData(LocalDate baseDate) throws Exception;
}