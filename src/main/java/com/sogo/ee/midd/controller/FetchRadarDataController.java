package com.sogo.ee.midd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sogo.ee.midd.exception.RadarAPIException;
import com.sogo.ee.midd.model.dto.WholeOrgTreeDto;
import com.sogo.ee.midd.service.APIEmployeeInfoService;
import com.sogo.ee.midd.service.APIOrganizationRelationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FetchRadarDataController {

	private final RestTemplate restTemplate;
	private final APIOrganizationRelationService apiOrganizationRelationService;
	private final APIEmployeeInfoService apiEmployeeInfoService;

	@Value("${radar.api.server.uri}")
	private String radarAPIServerURI;

	@Value("${radar.api.token}")
	private String radarAPIToken;

	@PostMapping("/organization-relations/sync")
	public ResponseEntity<String> syncOrganizationRelations(
			@RequestParam(name = "tree-type", defaultValue = "") String treeType) {
		try {
			String apiUrl = radarAPIServerURI + "/api/Org/OrganizationRelation";
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, "orgTreeTypies", treeType);
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
			@RequestParam(name = "employee-no", defaultValue = "") String employeeNo) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZEmployeeInfo";
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, "employeeNos", employeeNo);
			apiEmployeeInfoService.processEmployeeInfo(response);
			return ResponseEntity.ok("員工資訊同步成功");
		} catch (RadarAPIException e) {
			return ResponseEntity.badRequest().body("員工資訊同步失敗: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("員工資訊同步過程中發生未知錯誤");
		}
	}

	@GetMapping("/organization-relations")
	public ResponseEntity<?> getOrganizationRelations(
			@RequestParam(name = "tree-type", defaultValue = "0") String treeType) {
		try {
			List<WholeOrgTreeDto> orgRelations = apiOrganizationRelationService
					.fetchOrganizationRelationByorgTreeType(treeType);
			return ResponseEntity.ok(orgRelations);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("獲取組織關係時發生錯誤: " + e.getMessage());
		}
	}

	@PostMapping("/system/initialization")
	public ResponseEntity<String> initDatabase() {
		try {
			syncOrganizationRelations("");
			syncEmployeeInfo("");
			return ResponseEntity.ok("資料庫初始化成功");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("資料庫初始化失敗: " + e.getMessage());
		}
	}

	private ResponseEntity<String> fetchFromRadarAPI(String apiUrl, String paramName, String paramValue)
			throws RadarAPIException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-api-token", radarAPIToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
				.queryParam(paramName, paramValue);

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