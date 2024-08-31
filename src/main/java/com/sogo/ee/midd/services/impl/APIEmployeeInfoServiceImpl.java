package com.sogo.ee.midd.services.impl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
import com.sogo.ee.midd.repository.IAPIEmployeeInfoActionLogRepository;
import com.sogo.ee.midd.repository.IAPIEmployeeInfoArchivedRepository;
import com.sogo.ee.midd.repository.IAPIEmployeeInfoRepository;
import com.sogo.ee.midd.services.APIEmployeeInfoService;

@Service
public class APIEmployeeInfoServiceImpl implements APIEmployeeInfoService {

	private static final Logger logger = LoggerFactory.getLogger(APIEmployeeInfoServiceImpl.class);

	@Autowired
	private IAPIEmployeeInfoRepository employeeInfoRepo;

	@Autowired
	private IAPIEmployeeInfoArchivedRepository archivedRepo;

	@Autowired
	private IAPIEmployeeInfoActionLogRepository actionLogRepo;

	@Transactional
	public void processEmployeeInfo(ResponseEntity<String> response) throws Exception {

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
				logger.info("Table truncated");

				updateStatusForNewData(newEmployeeInfoList);
				logger.info("Status updated for new data");

				List<APIEmployeeInfo> savedList = employeeInfoRepo.saveAll(newEmployeeInfoList);
				logger.info("Saved new employee list size: " + savedList.size());

				// 驗證保存
				List<APIEmployeeInfo> verifiedList = employeeInfoRepo.findAll();
				logger.info("Verified saved employee list size: " + verifiedList.size());

				// // 生成操作日誌
				// List<APIEmployeeInfoActionLog> actionLogs = generateActionLogs();
				// actionLogRepo.saveAll(actionLogs);
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
		List<APIEmployeeInfo> currentList = employeeInfoRepo.findAll();
		List<APIEmployeeInfoArchived> archivedList = currentList.stream()
				.map(this::convertToArchived)
				.collect(Collectors.toList());
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

			// TODO: 這個比較方式, 由於每30分鐘會執行一次, 因此現在的判斷方式不夠完善, 但為了先處理後續開發, 待改善
			if (today.equals(createdDate) && today.equals(modifiedDate) && "1".equals(info.getEmployedStatus())) {
				info.setStatus("C");
			} else if (createdDate.isBefore(today) && modifiedDate.equals(today) && "1".equals(info.getEmployedStatus())) {
				info.setStatus("U");
			} else if (createdDate.isBefore(today) && modifiedDate.equals(today) && !"1".equals(info.getEmployedStatus())) {
				info.setStatus("D");
			}
		}
	}

	private List<APIEmployeeInfoActionLog> generateActionLogs() {

		List<APIEmployeeInfo> newList = employeeInfoRepo.findByEmployedStatus("1");
		List<APIEmployeeInfoArchived> archivedList = archivedRepo.findByEmployedStatus("1");

		System.out.println("Total elements in archivedList: " + archivedList.size());

		// 檢查 key 是否重複
		Map<String, Long> countMap = archivedList.stream()
				.collect(Collectors.groupingBy(APIEmployeeInfoArchived::getEmployeeNo, Collectors.counting()));

		countMap.entrySet().stream()
				.filter(entry -> entry.getValue() > 1)
				.forEach(entry -> System.out
						.println("Duplicate employeeNo found: " + entry.getKey() + ", count: " + entry.getValue()));

		Map<String, APIEmployeeInfoArchived> archivedMap = archivedList.stream()
				.collect(Collectors.toMap(APIEmployeeInfoArchived::getEmployeeNo, Function.identity()));

		List<APIEmployeeInfoActionLog> actionLogs = new ArrayList<>();

		for (APIEmployeeInfo newInfo : newList) {
			APIEmployeeInfoArchived oldInfo = archivedMap.remove(newInfo.getEmployeeNo());

			if (oldInfo == null) {
				// 新增操作
				actionLogs.add(new APIEmployeeInfoActionLog(newInfo.getEmployeeNo(), "C", "employee_no", null,
						newInfo.getEmployeeNo()));
			} else {
				// 更新操作
				compareAndLogChanges(newInfo, oldInfo, actionLogs);
			}
		}

		// 處理刪除操作
		for (APIEmployeeInfoArchived deletedInfo : archivedMap.values()) {
			actionLogs.add(new APIEmployeeInfoActionLog(deletedInfo.getEmployeeNo(), "D", "employee_no",
					deletedInfo.getEmployeeNo(), null));
		}

		return actionLogs;
	}

	private void compareAndLogChanges(APIEmployeeInfo newInfo, APIEmployeeInfoArchived oldInfo,
			List<APIEmployeeInfoActionLog> actionLogs) {
		Field[] fields = APIEmployeeInfo.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object newValue = field.get(newInfo);
				Object oldValue = field.get(oldInfo);

				// 檢查是否為基本型別 long，並進行轉換
				if (field.getType() == long.class && oldValue != null) {
					oldValue = ((Long) oldValue).longValue();
				}

				if (newValue != null && !newValue.equals(oldValue)) {
					actionLogs.add(new APIEmployeeInfoActionLog(
							newInfo.getEmployeeNo(),
							"U",
							field.getName(),
							oldValue != null ? oldValue.toString() : null,
							newValue.toString()));
				}
			} catch (IllegalAccessException e) {
				// 日誌記錄錯誤
				e.printStackTrace();
			}
		}
	}

}
