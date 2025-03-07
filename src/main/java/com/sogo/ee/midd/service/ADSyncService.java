package com.sogo.ee.midd.service;

import java.util.List;

import com.sogo.ee.midd.model.dto.ADEmployeeSyncDto;
import com.sogo.ee.midd.model.dto.ADOrganizationSyncDto;

public interface ADSyncService {
    public List<ADEmployeeSyncDto> getADEmployeeSyncData(String baseDate) throws Exception;

    public List<ADOrganizationSyncDto> getADOrganizationSyncData(String baseDate) throws Exception;

}