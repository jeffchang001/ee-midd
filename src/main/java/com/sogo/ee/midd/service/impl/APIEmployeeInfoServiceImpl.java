package com.sogo.ee.midd.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogo.ee.midd.model.dto.APIEmployeeInfoDto;
import com.sogo.ee.midd.model.entity.APIEmployeeInfo;
import com.sogo.ee.midd.model.entity.APIEmployeeInfoActionLog;
import com.sogo.ee.midd.model.entity.APIEmployeeInfoArchived;
import com.sogo.ee.midd.repository.APIEmployeeInfoActionLogRepository;
import com.sogo.ee.midd.repository.APIEmployeeInfoArchivedRepository;
import com.sogo.ee.midd.repository.APIEmployeeInfoRepository;
import com.sogo.ee.midd.service.APIEmployeeInfoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class APIEmployeeInfoServiceImpl implements APIEmployeeInfoService {

	@Autowired
	private APIEmployeeInfoRepository employeeInfoRepo;

	@Autowired
	private APIEmployeeInfoArchivedRepository archivedRepo;

	@Autowired
	private APIEmployeeInfoActionLogRepository actionLogRepo;

	@Transactional
	public void processEmployeeInfo(ResponseEntity<String> response) throws Exception {
		//需要有初始化的資料庫, 避免系統異常

		log.info("Starting processEmployeeInfo");

		try {
			// 解析 JSON
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			APIEmployeeInfoDto apiEmployeeInfoResponse = objectMapper.readValue(response.getBody(),
					APIEmployeeInfoDto.class);

			List<APIEmployeeInfo> newEmployeeInfoList = apiEmployeeInfoResponse.getResult();
			log.info("Parsed employee list size: "
					+ (newEmployeeInfoList != null ? newEmployeeInfoList.size() : "null"));

			if (newEmployeeInfoList != null && !newEmployeeInfoList.isEmpty()) {
				// 清空當前表格並插入新數據
				employeeInfoRepo.truncateTable();
				log.info("employeeInfoRepo Table truncated");

				// 儲存新數據
				List<APIEmployeeInfo> savedList = employeeInfoRepo.saveAll(newEmployeeInfoList);
				log.info("Saved new employee list size: " + savedList.size());

			} else {
				log.warn("No new employee data to process");
			}
		} catch (Exception e) {
			log.error("Error in processEmployeeInfo", e);
			throw e; // 重新拋出異常，確保事務 roll back
		}

		log.info("Completed processEmployeeInfo");

	}

	private APIEmployeeInfoArchived convertToArchived(APIEmployeeInfo info) {
		APIEmployeeInfoArchived archived = new APIEmployeeInfoArchived();
		// 使用反射複製所有欄位
		Field[] fields = APIEmployeeInfo.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Field archivedField = APIEmployeeInfoArchived.class.getDeclaredField(field.getName());
				archivedField.setAccessible(true);
				archivedField.set(archived, field.get(info));
			} catch (NoSuchFieldException | IllegalAccessException e) {
				// 日誌記錄錯誤
				e.printStackTrace();
			}
		}
		return archived;
	}

	@Transactional
	public void initEmployeeInfo(ResponseEntity<String> response) throws Exception {
		// 解析 JSON
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		APIEmployeeInfoDto apiEmployeeInfoResponse = objectMapper.readValue(response.getBody(),
				APIEmployeeInfoDto.class);

		List<APIEmployeeInfo> newEmployeeInfoList = apiEmployeeInfoResponse.getResult();
		
		// 清空當前表格並插入新數據
		employeeInfoRepo.truncateTable();
		employeeInfoRepo.saveAll(newEmployeeInfoList);

		List<APIEmployeeInfoArchived> archivedList = newEmployeeInfoList.stream()
				.map(this::convertToArchived)
				.collect(Collectors.toList());

		// 清空 archived 表格並插入新數據
		archivedRepo.truncateTable();
		archivedRepo.saveAll(archivedList);
	}

	@Transactional
	public void compareAndProcessEmployeeInfo(ResponseEntity<String> response) throws Exception {
		// Step 1: 取得並解析 json file
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		APIEmployeeInfoDto apiEmployeeInfoResponse = objectMapper.readValue(response.getBody(),
				APIEmployeeInfoDto.class);

		List<APIEmployeeInfo> employeeInfoList = apiEmployeeInfoResponse.getResult();
		log.info(employeeInfoList.size() + " 筆 json employee info received");

		// Step 2: 與資料庫比對每一筆資料, 先確認資料庫是否存在,; 若不存在, a) 新增資料, b) 則在 Acton Log 增加新增資訊;
		// 若存在：a) 更新資料, b) 則在 Acton Log 增加修改資訊
		APIEmployeeInfo dbEmployeeInfo = null;
		List<APIEmployeeInfoActionLog> actionLogList = new ArrayList<>();
		for (APIEmployeeInfo apiEmployeeInfo : employeeInfoList) {
			dbEmployeeInfo = employeeInfoRepo.findByEmployeeNo(apiEmployeeInfo.getEmployeeNo());
			if (dbEmployeeInfo == null) {
				// 新增員工的 email 強制改為 員工編號@sogo.com.tw
				apiEmployeeInfo.setEmailAddress(apiEmployeeInfo.getEmployeeNo() + "@sogo.com.tw");
				employeeInfoRepo.save(apiEmployeeInfo);
				APIEmployeeInfoActionLog actionLog = new APIEmployeeInfoActionLog(apiEmployeeInfo.getEmployeeNo(), "C",
						"employeeNo", null, apiEmployeeInfo.getEmployeeNo());
				actionLogList.add(actionLog);
			} else {
				Field[] fields = APIEmployeeInfo.class.getDeclaredFields();
				for (Field field : fields) {
					// 忽略 id, status, dateOfBirth createdDate
					if (field.getName().equals("id") || field.getName().equals("status")
							|| field.getName().equals("dateOfBirth") || field.getName().equals("createdDate")) {
						continue;
					}
					field.setAccessible(true);
					try {
						Object newValue = field.get(apiEmployeeInfo);

						// 嘗試從 DB:APIEmployeeInfo 獲取相同名稱的欄位
						Field oldField = null;
						try {
							oldField = APIEmployeeInfo.class.getDeclaredField(field.getName());
							oldField.setAccessible(true);
						} catch (NoSuchFieldException e) {
							// 如果欄位不存在，我們會跳過這個欄位的比較
							log.warn("Field {} not found in APIEmployeeInfo]", field.getName());
							continue;
						}

						Object oldValue = oldField.get(dbEmployeeInfo);

						// 若新舊值不同, 則在 Action Log 中增加修改資訊, 只要不同就先修改, 無論是否在職
						if (!Objects.equals(newValue, oldValue)) {
							actionLogList.add(new APIEmployeeInfoActionLog(
									apiEmployeeInfo.getEmployeeNo(),
									"U",
									field.getName(),
									oldValue != null ? oldValue.toString() : null,
									newValue != null ? newValue.toString() : null));
						}

					} catch (IllegalAccessException e) {
						log.error("Error accessing field: " + field.getName(), e);
					}
				}

				// 若在職狀態不同, 則在 Action Log 中增加刪除資訊
				if (!"1".equals(apiEmployeeInfo.getEmployedStatus()) && !"2".equals(apiEmployeeInfo.getEmployedStatus())
						&& !apiEmployeeInfo.getEmployedStatus().equals(dbEmployeeInfo.getEmployedStatus())) {
					APIEmployeeInfoActionLog actionLog = new APIEmployeeInfoActionLog(apiEmployeeInfo.getEmployeeNo(),
							"D",
							"employedStatus", dbEmployeeInfo.getEmployedStatus(), apiEmployeeInfo.getEmployedStatus());
					actionLogList.add(actionLog);
				}

				// 存在的話，將 apiEmployeeInfo 的屬性複製給 dbEmployeeInfo
				BeanUtils.copyProperties(apiEmployeeInfo, dbEmployeeInfo, "id", "status");
				employeeInfoRepo.save(dbEmployeeInfo);
			}
		}
		log.info("actionLogList info: " + actionLogList.toString());
		actionLogRepo.saveAll(actionLogList);
	}

}
