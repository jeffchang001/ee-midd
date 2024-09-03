package com.sogo.ee.midd.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sogo.ee.midd.model.dto.WholeOrgTreeDto;
import com.sogo.ee.midd.service.APIEmployeeInfoService;
import com.sogo.ee.midd.service.APIOrganizationRelationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FetchRadarDataController {

	private static final Logger logger = LoggerFactory.getLogger(FetchRadarDataController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${radar.api.server.uri}")
	private String radarAPIServerURI;

	@Value("${radar.api.token}")
	private String radarAPIToken;

	@Autowired
	private APIOrganizationRelationService apiOrganizationRelationService;

	@Autowired
	private APIEmployeeInfoService apiEmployeeInfoService;

	@PostMapping("/organization-relations/sync")
	public ResponseEntity<String> syncOrganizationRelations(
			@RequestParam(name = "tree-type", defaultValue = "") String treeType) {
		try {
			String apiUrl = radarAPIServerURI + "/api/Org/OrganizationRelation";

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-api-token", radarAPIToken);
			HttpEntity<String> entity = new HttpEntity<>(headers);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
					.queryParam("orgTreeTypies", treeType);

			ResponseEntity<String> response = restTemplate.exchange(
					builder.toUriString(),
					HttpMethod.GET,
					entity,
					String.class);

			apiOrganizationRelationService.processOrganizationRelation(response);

			return ResponseEntity.ok("Organization relations synced successfully");
		} catch (Exception e) {
			logger.error("Error syncing organization relations", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error syncing organization relations: " + e.getMessage());
		}
	}

	@PostMapping("/employees/sync")
	public ResponseEntity<String> syncEmployeeInfo(
			@RequestParam(name = "employee-no", defaultValue = "") String employeeNo) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZEmployeeInfo";

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-api-token", radarAPIToken);
			HttpEntity<String> entity = new HttpEntity<>(headers);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
					.queryParam("employeeNos", employeeNo);

			ResponseEntity<String> response = restTemplate.exchange(
					builder.toUriString(),
					HttpMethod.GET,
					entity,
					String.class);

			apiEmployeeInfoService.processEmployeeInfo(response);

			return ResponseEntity.ok("Employee information synced successfully");
		} catch (Exception e) {
			logger.error("Error syncing employee information", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error syncing employee information: " + e.getMessage());
		}
	}
	@GetMapping("/organization-relations")
	public ResponseEntity<List<WholeOrgTreeDto>> getOrganizationRelations(
			@RequestParam(name = "tree-type", defaultValue = "0") String treeType) {
		try {
			List<WholeOrgTreeDto> orgRelations = apiOrganizationRelationService
					.fetchOrganizationRelationByorgTreeType(treeType);
			return ResponseEntity.ok(orgRelations);
		} catch (Exception e) {
			logger.error("Error fetching organization relations", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}

	@PostMapping("/system/initialization")
	public ResponseEntity<String> initDatabase() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-api-token", radarAPIToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			// 處理組織關係資料
			processOrganizationRelation(entity);

			// 處理員工資訊
			processEmployeeInfo(entity);

			return ResponseEntity.ok("Database initialization successful");
		} catch (Exception e) {
			logger.error("Error during database initialization", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error during database initialization: " + e.getMessage());
		}
	}

	private void processOrganizationRelation(HttpEntity<String> entity) throws Exception {
		String apiOrgRelationUrl = radarAPIServerURI + "/api/Org/OrganizationRelation";
		UriComponentsBuilder orgRelationBuilder = UriComponentsBuilder.fromHttpUrl(apiOrgRelationUrl)
				.queryParam("orgTreeTypies", "");

		ResponseEntity<String> orgRelationResponse = restTemplate.exchange(
				orgRelationBuilder.toUriString(),
				HttpMethod.GET,
				entity,
				String.class);

		apiOrganizationRelationService.processOrganizationRelation(orgRelationResponse);
	}

	private void processEmployeeInfo(HttpEntity<String> entity) throws Exception {
		String apiEmployeeUrl = radarAPIServerURI + "/api/ZZApi/ZZEmployeeInfo";
		UriComponentsBuilder employeeBuilder = UriComponentsBuilder.fromHttpUrl(apiEmployeeUrl)
				.queryParam("employeeNos", "");

		ResponseEntity<String> employeeResponse = restTemplate.exchange(
				employeeBuilder.toUriString(),
				HttpMethod.GET, // 保持為 GET 方法，除非確定 API 支援 POST
				entity,
				String.class);

		apiEmployeeInfoService.processEmployeeInfo(employeeResponse);
	}

}
