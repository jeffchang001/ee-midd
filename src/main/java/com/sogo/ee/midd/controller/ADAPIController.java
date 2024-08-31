package com.sogo.ee.midd.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sogo.ee.midd.model.entity.APIEmployeeInfo;
import com.sogo.ee.midd.services.APIEmployeeInfoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class ADAPIController {
    private static final Logger logger = LoggerFactory.getLogger(ADAPIController.class);

    @Autowired
    private APIEmployeeInfoService apiEmployeeInfoService;

    @GetMapping("/ad-update-list")
    @ResponseBody
    public List<APIEmployeeInfo> getNonEmptyStatus() {
        return null;
    }

}
