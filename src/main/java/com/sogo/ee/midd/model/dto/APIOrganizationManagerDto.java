package com.sogo.ee.midd.model.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sogo.ee.midd.model.entity.APIOrganizationManager;

import lombok.Data;

@Data
public class APIOrganizationManagerDto {
    @JsonProperty("HttpStatusCode")
    private int httpStatusCode;

    @JsonProperty("ErrorCode")
    private String errorCode;

    @JsonProperty("Result")
    private List<APIOrganizationManager> result;

    @JsonProperty("ExtraData")
    private Map<String, String> ExtraData;
}
