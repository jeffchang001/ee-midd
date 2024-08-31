package com.sogo.ee.midd.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sogo.ee.midd.services.APIEmployeeInfoService;
// import com.sogo.ee.midd.services.IAPIOrganizationInfoService;
// import com.sogo.ee.midd.services.IAPIOrganizationRelationService;
import com.sogo.ee.midd.services.APIOrganizationRelationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FetchRadarDataController {

	private static final Logger logger = LoggerFactory.getLogger(FetchRadarDataController.class);

	@Value("${radar.api.server.uri}")
	private String radarAPIServerURI;

	@Value("${radar.api.token}")
	private String radarAPIToken;

	@Autowired
	private APIOrganizationRelationService apiOrganizationRelationService;

	@Autowired
	private APIEmployeeInfoService apiEmployeeInfoService;

	@GetMapping("/fetch-all-APIOrganizationRelation/{orgTreeType}")
	public String fetchAndSaveAPIOrganizationRelationData(@Autowired RestTemplate restTemplate,
			@PathVariable String orgTreeType) {

		String apiUrl = radarAPIServerURI + "/api/Org/OrganizationRelation";

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-api-token", radarAPIToken);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		if ("all".equals(orgTreeType)) {
			orgTreeType = "";
		}

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("orgTreeTypies",
				orgTreeType);

		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);

		try {
			apiOrganizationRelationService.processOrganizationRelation(response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "APIOrganizationRelation data fetched and saved successfully!";
	}

	@GetMapping("/fetch-all-APIEmployeeInfo/{employeeNo}")
	public String fetchAndSaveAPIEmployeeInfoData(@Autowired RestTemplate restTemplate,
			@PathVariable String employeeNo) {

		String apiUrl = radarAPIServerURI + "/api/Employee/EmployeeInfo";

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-api-token", radarAPIToken);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		if ("all".equals(employeeNo)) {
			employeeNo = "";
		}

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("employeeNos",
				employeeNo);

		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);

		try {
			apiEmployeeInfoService.processEmployeeInfo(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "APIEmployeeInfo data fetched and saved successfully!";
	}

}
