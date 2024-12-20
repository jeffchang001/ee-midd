package com.sogo.ee.midd.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDate;
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
		// TODO: 需要有初始化的資料庫, 避免系統異常

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
				// 步驟 1：將原 table 數據存至 Archived
				// List<APIEmployeeInfoArchived> archivedList = archiveCurrentData();
				// log.info("Archived list size: " + archivedList.size());

				// 步驟 2：清空當前表格並插入新數據
				employeeInfoRepo.truncateTable();
				log.info("employeeInfoRepo Table truncated");

				// 步驟 3：更新需要更新的資料狀態 (C:新增, U:更新, D:刪除)
				// updateStatusForNewData(newEmployeeInfoList);
				// log.info("Status updated for new data");

				// 步驟 4：保存新數據
				List<APIEmployeeInfo> savedList = employeeInfoRepo.saveAll(newEmployeeInfoList);
				log.info("Saved new employee list size: " + savedList.size());

				// 步驟 5：驗證筆數
				// List<APIEmployeeInfo> verifiedList = employeeInfoRepo.findAll();
				// log.info("Verified saved employee list size: " + verifiedList.size());

				// 步驟 6：產生操作日誌供 API 使用
				// generateActionLogs();

			} else {
				log.warn("No new employee data to process");
			}
		} catch (Exception e) {
			log.error("Error in processEmployeeInfo", e);
			throw e; // 重新拋出異常，確保事務 roll back
		}

		log.info("Completed processEmployeeInfo");

	}

	private List<APIEmployeeInfoArchived> archiveCurrentData() {
		List<APIEmployeeInfo> currentEmployeeInfoList = employeeInfoRepo.findAll();
		List<APIEmployeeInfoArchived> archivedList = currentEmployeeInfoList.stream()
				.map(this::convertToArchived)
				.collect(Collectors.toList());

		List<APIEmployeeInfoArchived> theAPIEmployeeInfoArchived = archivedRepo.findAll();

		log.debug("currentEmployeeInfoList===" + currentEmployeeInfoList.size());
		log.debug("archivedList===" + archivedList.size());
		log.debug("theAPIEmployeeInfoArchived===" + theAPIEmployeeInfoArchived.size());
		// 若 archived 有資料, 要先清除
		if (theAPIEmployeeInfoArchived.size() > 0) {
			archivedRepo.truncateTable();
			log.info("Archived table truncated");
		}

		return archivedRepo.saveAll(archivedList);
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

	private void updateStatusForNewData(List<APIEmployeeInfo> newList) {
		LocalDate today = LocalDate.now();
		for (APIEmployeeInfo info : newList) {
			LocalDate createdDate = info.getDataCreatedDate().toLocalDate();
			LocalDate modifiedDate = info.getDataModifiedDate().toLocalDate();

			log.info("employeeno============" + info.getEmployeeNo());
			log.info("today============" + today);
			log.info("createdDate============" + createdDate);
			log.info("modifiedDate============" + modifiedDate);
			log.info("createdDate.isBefore(today)============" + createdDate.isBefore(today));
			log.info("createdDate.equals(today)============" + createdDate.equals(today));
			log.info("modifiedDate.equals(today)============" + modifiedDate.equals(today));
			log.info("employedStatus============" + info.getEmployedStatus());

			// TODO: 這個比較方式, 由於每30分鐘會執行一次, 因此現在的判斷方式不夠完善, 但為了先處理後續開發, 待改善; 但後來發現 Radar
			// 一日只有兩次 1:00, 12:00 會跑排程更新, 因此可以確認一下做法
			if (today.equals(createdDate) && today.equals(modifiedDate) && "1".equals(info.getEmployedStatus())) {
				info.setStatus("C");
			} else if ((createdDate.isBefore(today) || createdDate.equals(today)) && modifiedDate.equals(today)
					&& "1".equals(info.getEmployedStatus())) {
				info.setStatus("U");
			} else if ((createdDate.isBefore(today) || createdDate.equals(today)) && modifiedDate.equals(today)
					&& !"1".equals(info.getEmployedStatus())) {
				info.setStatus("D");
			}

			log.info("info.getStatus()============" + info.getStatus());
			employeeInfoRepo.save(info);
		}
	}

	private void generateActionLogs() {
		List<APIEmployeeInfo> createAPIEmployeeInfoList = employeeInfoRepo.findByEmployedStatusAndStatus("1", "C");
		List<APIEmployeeInfo> updateAPIEmployeeInfoList = employeeInfoRepo.findByStatus("U");
		List<APIEmployeeInfo> deleteAPIEmployeeInfoList = employeeInfoRepo.findByStatus("D");

		APIEmployeeInfoArchived tmpEmployInfoArchived = null;
		APIEmployeeInfoActionLog tmpActionLog = null;
		List<APIEmployeeInfoActionLog> tmpActionLogList = null;

		// 處理可能需要新增的資料
		for (APIEmployeeInfo apiEmployeeInfo : createAPIEmployeeInfoList) {
			tmpEmployInfoArchived = archivedRepo.findByEmployeeNo(apiEmployeeInfo.getEmployeeNo());
			tmpActionLogList = actionLogRepo.findByEmployeeNoAndActionAndIsSync(apiEmployeeInfo.getEmployeeNo(), "C",
					Boolean.FALSE);
			if (tmpEmployInfoArchived == null && tmpActionLogList.isEmpty()) {
				// employeeInfo.status=C, 確認 archived 中沒有資料, 代表需要新增 actionLog
				tmpActionLog = new APIEmployeeInfoActionLog(apiEmployeeInfo.getEmployeeNo(), "C", "employee_no", null,
						apiEmployeeInfo.getEmployeeNo());
				actionLogRepo.save(tmpActionLog);
			} else {
				// employeeInfo.status=C, 且確認 archived 中有資料, actionLog 會有資料
				continue;
			}
		}

		// 處理可能需要更新的資料
		for (APIEmployeeInfo apiEmployeeInfo : updateAPIEmployeeInfoList) {
			tmpEmployInfoArchived = archivedRepo.findByEmployeeNo(apiEmployeeInfo.getEmployeeNo());
			tmpActionLogList = actionLogRepo.findByEmployeeNoAndActionAndIsSync(apiEmployeeInfo.getEmployeeNo(), "U",
					Boolean.FALSE);
			if (tmpEmployInfoArchived != null && tmpActionLogList.isEmpty()) {
				tmpActionLogList = new ArrayList<APIEmployeeInfoActionLog>();
				// Action 確認為更新資料, 存入 actionLog
				compareAndLogChanges(apiEmployeeInfo, tmpEmployInfoArchived, tmpActionLogList);
				actionLogRepo.saveAll(tmpActionLogList);
			} else {
				// 理論上不可能, 但若發生, 代表資料異常
				apiEmployeeInfo.setStatus("E");
				employeeInfoRepo.save(apiEmployeeInfo);
			}
		}

		// 處理可能需要刪除或停用的資料
		// TODO: 應該移除 D 的狀態, 需要都透過 U 的狀態, 去看 employeeStatus=2, 3 的時候, 增加停用的狀態即可
		for (APIEmployeeInfo apiEmployeeInfo : deleteAPIEmployeeInfoList) {
			// 代表第一次判斷 employeeInfo=D, 但在 emploeeInfoArchived 中已經有資料了
			tmpEmployInfoArchived = archivedRepo.findByEmployeeNo(apiEmployeeInfo.getEmployeeNo());
			tmpActionLogList = actionLogRepo.findByEmployeeNoAndActionAndIsSync(apiEmployeeInfo.getEmployeeNo(), "D",
					Boolean.FALSE);
			if (tmpEmployInfoArchived != null && tmpActionLogList.size() == 0) {
				// Action 確認為刪除資料, 存入 actionLog, 紀錄 employee status, 2=留停, 3=離職
				tmpActionLog = new APIEmployeeInfoActionLog(apiEmployeeInfo.getEmployeeNo(), "D", "employee_no",
						"EmployedStatus",
						apiEmployeeInfo.getEmployedStatus());
				actionLogRepo.save(tmpActionLog);
			} else {
				// do nothing
				continue;
			}
		}
		// 檢查 key 是否重複
		// Map<String, Long> countMap = archivedList.stream()
		// .collect(Collectors.groupingBy(APIEmployeeInfoArchived::getEmployeeNo,
		// Collectors.counting()));

		// countMap.entrySet().stream()
		// .filter(entry -> entry.getValue() > 1)
		// .forEach(entry -> logger.info("Duplicate employeeNo found: " + entry.getKey()
		// + ", count: " + entry.getValue()));

		// 處理刪除操作
		// for (APIEmployeeInfoArchived deletedInfo : archivedMap.values()) {
		// actionLogs.add(new APIEmployeeInfoActionLog(deletedInfo.getEmployeeNo(), "D",
		// "employee_no",
		// deletedInfo.getEmployeeNo(), null));
		// }
	}

	private void compareAndLogChanges(APIEmployeeInfo newInfo, APIEmployeeInfoArchived oldInfo,
			List<APIEmployeeInfoActionLog> actionLogs) {
		Field[] fields = APIEmployeeInfo.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("id") || field.getName().equals("status")) {
				// 忽略 id 和 status
				continue;
			}
			field.setAccessible(true);
			try {
				Object newValue = field.get(newInfo);

				// 嘗試從 APIEmployeeInfoArchived 獲取相同名稱的欄位
				Field oldField = null;
				try {
					oldField = APIEmployeeInfoArchived.class.getDeclaredField(field.getName());
					oldField.setAccessible(true);
				} catch (NoSuchFieldException e) {
					// 如果欄位不存在，我們會跳過這個欄位的比較
					log.warn("Field {} not found in APIEmployeeInfoArchived", field.getName());
					continue;
				}

				Object oldValue = oldField.get(oldInfo);

				if (!Objects.equals(newValue, oldValue)) {
					actionLogs.add(new APIEmployeeInfoActionLog(
							newInfo.getEmployeeNo(),
							"U",
							field.getName(),
							oldValue != null ? oldValue.toString() : null,
							newValue != null ? newValue.toString() : null));
				}
			} catch (IllegalAccessException e) {
				log.error("Error accessing field: " + field.getName(), e);
			}
		}
	}

	@Transactional
	public void initEmployeeInfo(ResponseEntity<String> response) throws Exception {
		// 解析 JSON
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		APIEmployeeInfoDto apiEmployeeInfoResponse = objectMapper.readValue(response.getBody(),
				APIEmployeeInfoDto.class);

		List<APIEmployeeInfo> newEmployeeInfoList = apiEmployeeInfoResponse.getResult();
		// 儲存employeeInfo資料
		employeeInfoRepo.truncateTable();
		employeeInfoRepo.saveAll(newEmployeeInfoList);

		List<APIEmployeeInfoArchived> archivedList = newEmployeeInfoList.stream()
				.map(this::convertToArchived)
				.collect(Collectors.toList());

		// 複製並儲存 employeeInfoArchived 資料
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

		// Step 2: 與資料庫比對每一筆資料, 先確認資料庫是否存在,; 若不存在, a) 新增資料, b) 則在 Acton Log 增加新增資訊;
		// 若存在：a) 更新資料, b) 則在 Acton Log 增加修改資訊
		APIEmployeeInfo dbEmployeeInfo = null;
		List<APIEmployeeInfoActionLog> actionLogList = new ArrayList<>();
		for (APIEmployeeInfo apiEmployeeInfo : employeeInfoList) {
			dbEmployeeInfo = employeeInfoRepo.findByEmployeeNo(apiEmployeeInfo.getEmployeeNo());
			if (dbEmployeeInfo == null) {
				employeeInfoRepo.save(apiEmployeeInfo);
				APIEmployeeInfoActionLog actionLog = new APIEmployeeInfoActionLog(apiEmployeeInfo.getEmployeeNo(), "C",
						"employee_no", null, apiEmployeeInfo.getEmployeeNo());
				actionLogList.add(actionLog);
			} else {
				Field[] fields = APIEmployeeInfo.class.getDeclaredFields();
				for (Field field : fields) {
					if (field.getName().equals("id") || field.getName().equals("status")) {
						// 忽略 id 和 status
						continue;
					}
					field.setAccessible(true);
					try {
						Object newValue = field.get(apiEmployeeInfo);

						// 嘗試從 APIEmployeeInfoArchived 獲取相同名稱的欄位
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

						// 若是非在職, 則代表需要刪除（停用）此員工
						if (!"1".equals(apiEmployeeInfo.getEmployedStatus())) {
							actionLogList.add(new APIEmployeeInfoActionLog(
									apiEmployeeInfo.getEmployeeNo(),
									"D",
									"employee_no",
									null,
									apiEmployeeInfo.getEmployeeNo()));
						}
					} catch (IllegalAccessException e) {
						log.error("Error accessing field: " + field.getName(), e);
					}
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
