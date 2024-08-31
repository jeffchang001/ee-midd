package com.sogo.ee.midd.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class APIEmployeeInfoServiceImpl implements APIEmployeeInfoService {

	private static final Logger logger = LoggerFactory.getLogger(APIEmployeeInfoServiceImpl.class);

	@Autowired
	private APIEmployeeInfoRepository employeeInfoRepo;

	@Autowired
	private APIEmployeeInfoArchivedRepository archivedRepo;

	@Autowired
	private APIEmployeeInfoActionLogRepository actionLogRepo;

	@Transactional
	public void processEmployeeInfo(ResponseEntity<String> response) throws Exception {
		// TODO: 需要有初始化的資料庫, 避免系統異常

		logger.info("Starting processEmployeeInfo");

		try {
			// 解析 JSON
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			APIEmployeeInfoDto apiEmployeeInfoResponse = objectMapper.readValue(response.getBody(),
					APIEmployeeInfoDto.class);

			List<APIEmployeeInfo> newEmployeeInfoList = apiEmployeeInfoResponse.getResult();
			logger.info("Parsed employee list size: "
					+ (newEmployeeInfoList != null ? newEmployeeInfoList.size() : "null"));

			if (newEmployeeInfoList != null && !newEmployeeInfoList.isEmpty()) {
				// 步驟 1：將當前數據存檔
				List<APIEmployeeInfoArchived> archivedList = archiveCurrentData();
				logger.info("Archived list size: " + archivedList.size());

				// 步驟 2：清空當前表格並插入新數據
				employeeInfoRepo.truncateTable();
				logger.info("employeeInfoRepo Table truncated");

				updateStatusForNewData(newEmployeeInfoList);
				logger.info("Status updated for new data");

				List<APIEmployeeInfo> savedList = employeeInfoRepo.saveAll(newEmployeeInfoList);
				logger.info("Saved new employee list size: " + savedList.size());

				// 驗證保存
				List<APIEmployeeInfo> verifiedList = employeeInfoRepo.findAll();
				logger.info("Verified saved employee list size: " + verifiedList.size());

				// // 生成操作日誌
				generateActionLogs();

			} else {
				logger.warn("No new employee data to process");
			}
		} catch (Exception e) {
			logger.error("Error in processEmployeeInfo", e);
			throw e; // 重新拋出異常，確保事務回滾
		}

		logger.info("Completed processEmployeeInfo");

	}

	private List<APIEmployeeInfoArchived> archiveCurrentData() {
		List<APIEmployeeInfo> currentEmployeeInfoList = employeeInfoRepo.findAll();
		List<APIEmployeeInfoArchived> archivedList = currentEmployeeInfoList.stream()
				.map(this::convertToArchived)
				.collect(Collectors.toList());

		List<APIEmployeeInfoArchived> theAPIEmployeeInfoArchived = archivedRepo.findAll();

		System.out.println("currentEmployeeInfoList===" + currentEmployeeInfoList.size());
		System.out.println("archivedList===" + archivedList.size());
		System.out.println("theAPIEmployeeInfoArchived===" + theAPIEmployeeInfoArchived.size());
		// 若 archived 有資料, 要先清除
		if (theAPIEmployeeInfoArchived.size() > 0) {
			archivedRepo.truncateTable();
			logger.info("Archived table truncated");
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

			
			logger.info("employeeno============"+info.getEmployeeNo());
			logger.info("today============"+today);
			logger.info("createdDate============"+createdDate);
			logger.info("modifiedDate============"+modifiedDate);
			logger.info("createdDate.isBefore(today)============"+createdDate.isBefore(today));
			logger.info("createdDate.equals(today)============"+createdDate.equals(today));
			logger.info("modifiedDate.equals(today)============"+modifiedDate.equals(today));
			logger.info("employedStatus============"+info.getEmployedStatus());
			

			// TODO: 這個比較方式, 由於每30分鐘會執行一次, 因此現在的判斷方式不夠完善, 但為了先處理後續開發, 待改善
			if (today.equals(createdDate) && today.equals(modifiedDate) && "1".equals(info.getEmployedStatus())) {
				logger.info("ccc");
				info.setStatus("C");
			} else if ((createdDate.isBefore(today) || createdDate.equals(today)) && modifiedDate.equals(today) && "1".equals(info.getEmployedStatus())) {
				info.setStatus("U");
				logger.info("uuu");
			} else if ((createdDate.isBefore(today) || createdDate.equals(today)) && modifiedDate.equals(today) && !"1".equals(info.getEmployedStatus())) {
				info.setStatus("D");
				logger.info("ddd");
			}
			
			logger.info("info.getStatus()============"+info.getStatus());
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
			if (tmpEmployInfoArchived == null) {
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
			if (tmpEmployInfoArchived != null) {
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

		// 處理可能需要刪除的資料
		for (APIEmployeeInfo apiEmployeeInfo : deleteAPIEmployeeInfoList) {
			// 代表第一次判斷 employeeInfo=D, 但在 emploeeInfoArchived 中已經有資料了
			tmpEmployInfoArchived = archivedRepo.findByEmployeeNo(apiEmployeeInfo.getEmployeeNo());
			tmpActionLogList = actionLogRepo.findByEmployeeNoAndActionAndIsSync(apiEmployeeInfo.getEmployeeNo(), "D",
					Boolean.FALSE);
			if (tmpEmployInfoArchived != null && tmpActionLogList.size() == 0) {
				// Action 確認為刪除資料, 存入 actionLog
				tmpActionLog = new APIEmployeeInfoActionLog(apiEmployeeInfo.getEmployeeNo(), "D", "employee_no", null,
						null);
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
					logger.warn("Field {} not found in APIEmployeeInfoArchived", field.getName());
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
				logger.error("Error accessing field: " + field.getName(), e);
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
}
