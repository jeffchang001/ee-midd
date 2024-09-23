package com.sogo.ee.midd.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sogo.ee.midd.exception.RadarAPIException;
import com.sogo.ee.midd.service.APICompanyService;
import com.sogo.ee.midd.service.APIEmployeeInfoService;
import com.sogo.ee.midd.service.APIOrganizationManagerService;
import com.sogo.ee.midd.service.APIOrganizationRelationService;
import com.sogo.ee.midd.service.APIOrganizationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FetchRadarDataController {

	private final RestTemplate restTemplate;
	private final APICompanyService apiCompanyService;
	private final APIOrganizationService apiOrganizationService;
	private final APIOrganizationManagerService apiOrganizationManagerService;
	private final APIOrganizationRelationService apiOrganizationRelationService;
	private final APIEmployeeInfoService apiEmployeeInfoService;

	@Value("${radar.api.server.uri}")
	private String radarAPIServerURI;

	@Value("${radar.api.token}")
	private String radarAPIToken;

	@PostMapping("/company/sync")
	public ResponseEntity<String> syncCompany() {
		try {
			String apiUrl = radarAPIServerURI + "/api/Org/Company";
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, null);
			apiCompanyService.processCompany(response);
			return ResponseEntity.ok("公司資訊同步成功");
		} catch (RadarAPIException e) {
			return ResponseEntity.badRequest().body("公司資訊同步失敗: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("公司資訊同步過程中發生未知錯誤");
		}
	}

	@PostMapping("/organization/sync")
	public ResponseEntity<String> syncOrganization(
			@RequestParam(name = "org-codes", defaultValue = "") String orgCodes) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZOrganization";
			Map<String, String> params = new HashMap<>();
			params.put("orgCodes", orgCodes);
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiOrganizationService.processOrganization(response);
			return ResponseEntity.ok("組織資訊同步成功");
		} catch (RadarAPIException e) {
			return ResponseEntity.badRequest().body("組織資訊同步失敗: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("組織資訊同步過程中發生未知錯誤");
		}
	}

	@PostMapping("/organization-manager/sync")
	public ResponseEntity<String> syncOrganizationManager(
			@RequestParam(name = "org-codes", defaultValue = "") String orgCodes) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZOrganizationManager";
			Map<String, String> params = new HashMap<>();
			params.put("orgCodes", orgCodes);
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiOrganizationManagerService.processOrganizationManager(response);
			return ResponseEntity.ok("單位主管同步成功");
		} catch (RadarAPIException e) {
			return ResponseEntity.badRequest().body("單位主管同步失敗: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("單位主管同步過程中發生未知錯誤");
		}
	}

	@PostMapping("/organization-relations/sync")
	public ResponseEntity<String> syncOrganizationRelation(
			@RequestParam(name = "tree-type", defaultValue = "") String treeType) {
		try {
			String apiUrl = radarAPIServerURI + "/api/Org/OrganizationRelation";
			Map<String, String> params = new HashMap<>();
			params.put("orgTreeTypies", treeType);
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiOrganizationRelationService.processOrganizationRelation(response);
			return ResponseEntity.ok("組織關係同步成功");
		} catch (RadarAPIException e) {
			return ResponseEntity.badRequest().body("組織關係同步失敗: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("組織關係同步過程中發生未知錯誤");
		}
	}

	@PostMapping("/employees/sync")
	public ResponseEntity<String> syncEmployeeInfo(
			@RequestParam(name = "employee-no", defaultValue = "") String employeeNo,
			@RequestParam(name = "base-date", defaultValue = "") String baseDate) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZEmployeeInfo";
			Map<String, String> params = new HashMap<>();
			params.put("employeeNos", employeeNo);
			params.put("baseDate", baseDate);
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiEmployeeInfoService.processEmployeeInfo(response);
			return ResponseEntity.ok("員工資訊同步成功");
		} catch (RadarAPIException e) {
			return ResponseEntity.badRequest().body("員工資訊同步失敗: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("員工資訊同步過程中發生未知錯誤");
		}
	}

	// @GetMapping("/organization-relations")
	// public ResponseEntity<?> OrganizationRelations(
	// 		@RequestParam(name = "tree-type", defaultValue = "") String treeType) {
	// 	try {
	// 		List<WholeOrgTreeDto> orgRelations = apiOrganizationRelationService
	// 				.fetchOrganizationRelationByorgTreeType(treeType);
	// 		return ResponseEntity.ok(orgRelations);
	// 	} catch (Exception e) {
	// 		return ResponseEntity.internalServerError().body("獲取組織關係時發生錯誤: " + e.getMessage());
	// 	}
	// }

	@PostMapping("/system/initialization")
	public ResponseEntity<String> initDatabase() {
		try {
			syncCompany();
			syncOrganization("");
			syncOrganizationManager("");
			syncOrganizationRelation("");
			syncEmployeeInfo("", "");
			return ResponseEntity.ok("資料庫初始化成功");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("資料庫初始化失敗: " + e.getMessage());
		}
	}

	private ResponseEntity<String> fetchFromRadarAPI(String apiUrl, Map<String, String> params)
			throws RadarAPIException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-api-token", radarAPIToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl);
		if (params != null) {
			params.forEach(builder::queryParam);
		}

		try {
			return restTemplate.exchange(
					builder.toUriString(),
					HttpMethod.GET,
					entity,
					String.class);
		} catch (RestClientException e) {
			throw new RadarAPIException("無法從 Radar API 獲取數據: " + e.getMessage(), e);
		}
	}
}