package com.sogo.ee.midd.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sogo.ee.midd.model.dto.MaterializedViewChangeDto;
import com.sogo.ee.midd.service.MaterializedViewChangeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 實體化視圖變更資訊服務實現類
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MaterializedViewChangeServiceImpl implements MaterializedViewChangeService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根據日期和視圖名稱查詢實體化視圖變更資訊
     *
     * @param date     查詢日期
     * @param viewName 視圖名稱 (可選)
     * @return 實體化視圖變更資訊列表
     */
    @Override
    public List<MaterializedViewChangeDto> getViewChangesByDate(LocalDate date, String viewName) {
        String sql;
        Object[] params;

        if (viewName != null && !viewName.isEmpty()) {
            sql = "SELECT view_name, refreshed_at, diff_count, diff_details::text as diff_details_text FROM get_materialized_view_changes_by_date(?, ?)";
            params = new Object[] { date, viewName };
        } else {
            sql = "SELECT view_name, refreshed_at, diff_count, diff_details::text as diff_details_text FROM get_materialized_view_changes_by_date(?, NULL)";
            params = new Object[] { date };
        }

        log.info("執行查詢: {}, 參數: date={}, viewName={}", sql, date, viewName);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            MaterializedViewChangeDto dto = new MaterializedViewChangeDto();
            dto.setViewName(rs.getString("view_name"));
            dto.setRefreshedAt(rs.getTimestamp("refreshed_at").toLocalDateTime());
            dto.setDiffCount(rs.getInt("diff_count"));

            // 處理 JSONB 類型
            try {
                // 獲取轉換為文本的 JSONB 數據
                String jsonStr = rs.getString("diff_details_text");

                if (jsonStr != null && !jsonStr.isEmpty() && !jsonStr.equals("null")) {
                    // 將 JSON 字符串解析為 Java 對象，並轉換為駝峰式命名法
                    Object diffDetails = convertToCamelCase(jsonStr);
                    dto.setDiffDetails(diffDetails);
                } else {
                    dto.setDiffDetails(null);
                }
            } catch (Exception e) {
                log.error("解析 JSONB 數據時發生錯誤: {}", e.getMessage());
                dto.setDiffDetails(null);
            }

            return dto;
        });
    }

    /**
     * 將 JSON 字符串解析為 Java 對象，並轉換為駝峰式命名法
     *
     * @param jsonStr JSON 字符串
     * @return Java 對象
     */
    private Object convertToCamelCase(String jsonStr) throws IOException {
        try {
            // 嘗試解析為 JsonNode
            JsonNode jsonNode = objectMapper.readTree(jsonStr);

            // 轉換為駝峰式命名法
            if (jsonNode.isObject()) {
                return convertObjectToCamelCase((ObjectNode) jsonNode);
            } else if (jsonNode.isArray()) {
                // 處理數組
                for (int i = 0; i < jsonNode.size(); i++) {
                    JsonNode element = jsonNode.get(i);
                    if (element.isObject()) {
                        ((com.fasterxml.jackson.databind.node.ArrayNode) jsonNode).set(i,
                                convertObjectToCamelCase((ObjectNode) element));
                    }
                }
                return objectMapper.treeToValue(jsonNode, Object.class);
            } else {
                // 如果是基本類型，直接返回
                if (jsonNode.isTextual()) {
                    return jsonNode.asText();
                } else if (jsonNode.isNumber()) {
                    return jsonNode.numberValue();
                } else if (jsonNode.isBoolean()) {
                    return jsonNode.booleanValue();
                } else {
                    return null;
                }
            }
        } catch (JsonProcessingException e) {
            log.error("JSON 解析錯誤: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 將 ObjectNode 的屬性名稱轉換為駝峰式命名法
     *
     * @param objectNode ObjectNode
     * @return 轉換後的 ObjectNode
     */
    private ObjectNode convertObjectToCamelCase(ObjectNode objectNode) throws IOException {
        ObjectNode result = objectMapper.createObjectNode();
        Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            // 轉換屬性名稱為駝峰式命名法
            String camelCaseKey = toCamelCase(key);

            // 遞迴處理嵌套對象
            if (value.isObject()) {
                result.set(camelCaseKey, convertObjectToCamelCase((ObjectNode) value));
            } else if (value.isArray()) {
                // 處理數組
                com.fasterxml.jackson.databind.node.ArrayNode arrayNode = objectMapper.createArrayNode();
                for (int i = 0; i < value.size(); i++) {
                    JsonNode element = value.get(i);
                    if (element.isObject()) {
                        arrayNode.add(convertObjectToCamelCase((ObjectNode) element));
                    } else {
                        arrayNode.add(element);
                    }
                }
                result.set(camelCaseKey, arrayNode);
            } else {
                // 直接設置值
                result.set(camelCaseKey, value);
            }
        }

        return result;
    }

    /**
     * 將下劃線命名法轉換為駝峰式命名法
     *
     * @param underscoreStr 下劃線命名法字符串
     * @return 駝峰式命名法字符串
     */
    private String toCamelCase(String underscoreStr) {
        if (underscoreStr == null || underscoreStr.isEmpty()) {
            return underscoreStr;
        }

        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;

        for (int i = 0; i < underscoreStr.length(); i++) {
            char c = underscoreStr.charAt(i);

            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(c);
                }
            }
        }

        return result.toString();
    }

    @Override
    @Transactional
    public void refreshAllMaterializedViews() {
        log.info("開始重新整理所有 materialized views");
        try {
            jdbcTemplate.execute("SELECT refresh_all_materialized_views()");
            log.info("成功重新整理所有 materialized views");
        } catch (Exception e) {
            log.error("重新整理 materialized views 時發生錯誤", e);
            throw new RuntimeException("重新整理 materialized views 失敗", e);
        }
    }
}