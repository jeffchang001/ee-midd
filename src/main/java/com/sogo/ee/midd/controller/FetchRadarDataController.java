package com.sogo.ee.midd.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.sogo.ee.midd.service.APICompanyService;
import com.sogo.ee.midd.service.APIEmployeeInfoService;
import com.sogo.ee.midd.service.APIOrganizationManagerService;
import com.sogo.ee.midd.service.APIOrganizationRelationService;
import com.sogo.ee.midd.service.APIOrganizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Radar Data Sync", description = "提供從 Radar API 同步數據的功能")
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

	@PostMapping("/companies/initial-sync")
	@Operation(summary = "初始化同步公司資訊", description = "從 Radar API 同步公司資訊")
	@ApiResponse(responseCode = "200", description = "公司資訊同步成功")
	@ApiResponse(responseCode = "400", description = "公司資訊同步失敗")
	@ApiResponse(responseCode = "500", description = "內部伺服器錯誤")
	public ResponseEntity<String> initialSyncCompany() {
		try {
			String apiUrl = radarAPIServerURI + "/api/Org/Company";
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, null);
			apiCompanyService.processCompany(response);
			return ResponseEntity.ok("公司資訊同步成功");
		} catch (RadarAPIException e) {
			log.error("Radar api Error", e);
			return ResponseEntity.badRequest().body("公司資訊同步失敗: " + e.getMessage());
		} catch (Exception e) {
			log.error("公司資訊同步過程中發生未知錯誤", e);
			return ResponseEntity.internalServerError().body("公司資訊同步過程中發生未知錯誤");
		}
	}

	@PostMapping("/organizations/initial-sync")
	@Operation(summary = "初始化同步組織資訊", description = "從 Radar API 同步組織資訊")
	@ApiResponse(responseCode = "200", description = "組織資訊同步成功")
	@ApiResponse(responseCode = "400", description = "組織資訊同步失敗")
	@ApiResponse(responseCode = "500", description = "內部伺服器錯誤")
	public ResponseEntity<String> initialSyncOrganization(
			@Parameter(description = "組織代碼") @RequestParam(name = "org-code", defaultValue = "") String orgCode,
			@Parameter(description = "基準日期：日期之後的資料") @RequestParam(name = "base-date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZOrganization";
			Map<String, String> params = new HashMap<>();
			params.put("orgCodes", orgCode);
			params.put("baseDate", baseDate != null ? baseDate.toString().trim() : "");
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiOrganizationService.processOrganization(response);
			return ResponseEntity.ok("組織資訊同步成功");
		} catch (RadarAPIException e) {
			log.error("Radar api Error", e);
			return ResponseEntity.badRequest().body("組織資訊同步失敗: " + e.getMessage());
		} catch (Exception e) {
			log.error("組織資訊同步過程中發生未知錯誤", e);
			return ResponseEntity.internalServerError().body("組織資訊同步過程中發生未知錯誤");
		}
	}

	@PostMapping("/organization-managers/initial-sync")
	@Operation(summary = "初始化同步組織管理者資訊", description = "從 Radar API 同步組織管理者資訊")
	@ApiResponse(responseCode = "200", description = "組織管理者資訊同步成功")
	@ApiResponse(responseCode = "400", description = "組織管理者資訊同步失敗")
	@ApiResponse(responseCode = "500", description = "內部伺服器錯誤")
	public ResponseEntity<String> initialSyncOrganizationManager(
			@Parameter(description = "組織代碼") @RequestParam(name = "org-code", defaultValue = "") String orgCode,
			@Parameter(description = "基準日期：日期之後的資料") @RequestParam(name = "base-date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZOrganizationManager";
			Map<String, String> params = new HashMap<>();
			params.put("orgCodes", orgCode);
			params.put("baseDate", baseDate != null ? baseDate.toString().trim() : "");
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiOrganizationManagerService.processOrganizationManager(response);
			return ResponseEntity.ok("組織管理者資訊同步成功");
		} catch (RadarAPIException e) {
			log.error("Radar api Error", e);
			return ResponseEntity.badRequest().body("組織管理者資訊同步失敗: " + e.getMessage());
		} catch (Exception e) {
			log.error("組織管理者資訊同步過程中發生未知錯誤", e);
			return ResponseEntity.internalServerError().body("組織管理者資訊同步過程中發生未知錯誤");
		}
	}

	@PostMapping("/organization-relations/initial-sync")
	@Operation(summary = "初始化同步組織關係", description = "從 Radar API 同步組織關係")
	@ApiResponse(responseCode = "200", description = "組織關係同步成功")
	@ApiResponse(responseCode = "400", description = "組織關係同步失敗")
	@ApiResponse(responseCode = "500", description = "內部伺服器錯誤")
	public ResponseEntity<String> initialSyncOrganizationRelation(
			@Parameter(description = "樹狀結構類型") @RequestParam(name = "tree-type", defaultValue = "") String treeType) {
		try {
			String apiUrl = radarAPIServerURI + "/api/Org/OrganizationRelation";
			Map<String, String> params = new HashMap<>();
			params.put("orgTreeTypies", treeType);
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiOrganizationRelationService.processOrganizationRelation(response);
			return ResponseEntity.ok("組織關係同步成功");
		} catch (RadarAPIException e) {
			log.error("Radar api Error", e);
			return ResponseEntity.badRequest().body("組織關係同步失敗: " + e.getMessage());
		} catch (Exception e) {
			log.error("組織關係同步過程中發生未知錯誤", e);
			return ResponseEntity.internalServerError().body("組織關係同步過程中發生未知錯誤");
		}
	}

	@PostMapping("/employees/initial-sync")
	@Operation(summary = "初始化同步員工資訊", description = "從 Radar API 同步員工資訊")
	@ApiResponse(responseCode = "200", description = "員工資訊同步成功")
	@ApiResponse(responseCode = "400", description = "員工資訊同步失敗")
	@ApiResponse(responseCode = "500", description = "內部伺服器錯誤")
	public ResponseEntity<String> initialSyncEmployeeInfo(
			@Parameter(description = "員工編號") @RequestParam(name = "employee-no", defaultValue = "") String employeeNo,
			@Parameter(description = "基準日期：日期之後的資料") @RequestParam(name = "base-date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZEmployeeInfo";
			Map<String, String> params = new HashMap<>();
			params.put("employeeNos", employeeNo);
			params.put("baseDate", baseDate != null ? baseDate.toString() : "");
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiEmployeeInfoService.processEmployeeInfo(response);
			return ResponseEntity.ok("員工資訊同步成功");
		} catch (RadarAPIException e) {
			log.error("Radar api Error", e);
			return ResponseEntity.badRequest().body("員工資訊同步失敗: " + e.getMessage());
		} catch (Exception e) {
			log.error("員工資訊同步過程中發生未知錯誤", e);
			return ResponseEntity.internalServerError().body("員工資訊同步過程中發生未知錯誤");
		}
	}

	@GetMapping("/organization-relations/fetch")
	@Operation(summary = "獲取組織關係", description = "獲取指定樹狀結構類型的組織關係")
	@ApiResponse(responseCode = "200", description = "成功獲取組織關係")
	@ApiResponse(responseCode = "500", description = "內部伺服器錯誤")
	public ResponseEntity<List<WholeOrgTreeDto>> fetchOrganizationRelations(
			@Parameter(description = "樹狀結構類型") @RequestParam(name = "tree-type", defaultValue = "") String treeType) {
		try {
			List<WholeOrgTreeDto> orgRelations = apiOrganizationRelationService
					.fetchOrganizationRelationByorgTreeType(treeType);
			return ResponseEntity.ok(orgRelations);
		} catch (Exception e) {
			log.error("獲取組織關係時發生錯誤", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/organization-managers/check")
	@Operation(summary = "檢查是否為組織管理者", description = "檢查指定員工是否為指定組織的管理者")
	@ApiResponse(responseCode = "200", description = "成功檢查組織管理者狀態")
	@ApiResponse(responseCode = "500", description = "內部伺服器錯誤")
	public ResponseEntity<Boolean> isOrgManager(
			@Parameter(description = "員工編號") @RequestParam(name = "employee-no", defaultValue = "") String employeeNo,
			@Parameter(description = "組織代碼") @RequestParam(name = "org-code", defaultValue = "") String orgCode) {
		try {
			boolean isManager = apiOrganizationManagerService.existsByEmployeeNoAndOrgCode(employeeNo, orgCode);
			return ResponseEntity.ok(isManager);
		} catch (Exception e) {
			log.error("檢查組織管理者狀態時發生錯誤", e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@PostMapping("/system/initialization")
	@Operation(summary = "初始化系統", description = "執行系統初始化，同步所有必要的數據")
	@ApiResponse(responseCode = "200", description = "系統初始化成功")
	@ApiResponse(responseCode = "500", description = "系統初始化失敗")
	public ResponseEntity<String> initDatabase() {
		try {
			// 此同步數據順序也應該是未來各自 api 被呼叫的順序
			initialSyncCompany();
			initialSyncOrganization("", null);
			initialSyncOrganizationRelation("");
			initialSyncOrganizationManager("", null);
			initialSyncEmployeeInfo("", null);
			return ResponseEntity.ok("資料庫初始化成功");
		} catch (Exception e) {
			log.error("資料庫初始化失敗", e);
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
			log.error("無法從 Radar API 獲取數據", e);
			throw new RadarAPIException("無法從 Radar API 獲取數據: " + e.getMessage(), e);
		}
	}

	@PostMapping("/organizations/fetch-compare")
	@Operation(summary = "取得並比對組織資訊", description = "取得指定日期後的組織資訊, 比對後產生同步 AD 資料")
	@ApiResponse(responseCode = "200", description = "取得並比對組織資訊步成功")
	@ApiResponse(responseCode = "400", description = "取得並比對組織資訊失敗")
	@ApiResponse(responseCode = "500", description = "內部伺服器錯誤")
	public ResponseEntity<String> fetchCompareOrganization(
			@Parameter(description = "組織代碼") @RequestParam(name = "org-code", defaultValue = "") String orgCode,
			@Parameter(description = "基準日期：指定日期之後的資料", schema = @Schema(type = "string", format = "date", example = "2024-09-25")) 
			@RequestParam(name = "base-date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZOrganization";
			Map<String, String> params = new HashMap<>();
			params.put("orgCodes", orgCode);
			params.put("baseDate", baseDate.toString().trim());
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiOrganizationService.compareAndProcessOrganization(response);
			return ResponseEntity.ok("取得並比對組織資訊成功");
		} catch (RadarAPIException e) {
			log.error("Radar api Error", e);
			return ResponseEntity.badRequest().body("取得並比對組織資訊失敗: " + e.getMessage());
		} catch (Exception e) {
			log.error("取得並比對組織資訊過程中發生未知錯誤", e);
			return ResponseEntity.internalServerError().body("取得並比對組織資訊過程中發生未知錯誤");
		}
	}

	@PostMapping("/employees/fetch-compare")
	@Operation(summary = "取得並比對員工資訊", description = "取得指定日期後的員工資訊, 比對後產生同步 AD 資料\"")
	@ApiResponse(responseCode = "200", description = "取得並比對成功")
	@ApiResponse(responseCode = "400", description = "取得並比對失敗")
	@ApiResponse(responseCode = "500", description = "內部伺服器錯誤")
	public ResponseEntity<String> fetchCompareEmployeeInfo(
			@Parameter(description = "員工編號") @RequestParam(name = "employee-no", defaultValue = "") String employeeNo,
			@Parameter(description = "基準日期：日期之後的資料", schema = @Schema(type = "string", format = "date", example = "2024-09-25")) 
			@RequestParam(name = "base-date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate) {
		try {
			String apiUrl = radarAPIServerURI + "/api/ZZApi/ZZEmployeeInfo";
			Map<String, String> params = new HashMap<>();
			params.put("employeeNos", employeeNo);
			params.put("baseDate", baseDate != null ? baseDate.toString().trim() : "");
			ResponseEntity<String> response = fetchFromRadarAPI(apiUrl, params);
			apiEmployeeInfoService.compareAndProcessEmployeeInfo(response);
			return ResponseEntity.ok("取得並比對員工資訊成功");
		} catch (RadarAPIException e) {
			log.error("Radar api Error", e);
			return ResponseEntity.badRequest().body("取得並比對員工資訊失敗: " + e.getMessage());
		} catch (Exception e) {
			log.error("取得並比對員工資訊過程中發生未知錯誤", e);
			return ResponseEntity.internalServerError().body("取得並比對員工資訊過程中發生未知錯誤");
		}
	}

}