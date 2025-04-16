# SOGO 員工中台系統 (Employee Middleware)

## 專案介紹

SOGO 員工中台系統是一個整合性的員工資料管理平台，提供統一的介面來管理員工資料、組織結構和 AD 帳戶。本系統作為中介層，連接 HR 系統（Radar API）和 AD 系統，確保資料的同步和一致性。

### 系統關聯

本系統（ee-midd）與 AD 中台系統（ad-midd）之間的關係：

1. 資料流向
   - ee-midd 負責從 Radar API 獲取員工和組織資料
   - ee-midd 處理資料轉換和驗證
   - ee-midd 提供 API 供 ad-midd 調用
   - ad-midd 負責將資料同步至 Azure AD

2. 系統職責
   - ee-midd：
     * 員工資料主資料管理
     * 組織架構管理
     * 資料轉換和驗證
     * 提供標準化 API
   - ad-midd：
     * AD 帳戶操作
     * OU 結構管理
     * AD 權限管理
     * AD 同步作業

3. API 調用關係
   - ad-midd 透過以下 API 從 ee-midd 獲取資料：
     * `/api/v1/ad-employee-sync-data`：取得員工同步資料
     * `/api/v1/ad-organization-sync-data`：取得組織同步資料
     * `/api/v1/empty-dns`：取得空組織 DN 列表

### 主要功能

- 🔄 員工資料同步管理
  - 與 Radar API 系統整合
  - 自動同步員工基本資料
  - 組織架構同步
  - 職位和職等管理

- 👥 AD 帳戶管理
  - 自動建立/停用 AD 帳戶
  - 組織單位(OU)同步
  - 權限管理

- 📊 資料整合服務
  - RESTful API 服務
  - 物化視圖優化
  - 資料一致性保證

### 系統特色

- 遵循 Google RESTful API 設計規範
- 完整的 API 文件（Swagger UI）
- 容器化部署支援
- 高效能資料同步機制

## 系統架構

### 技術棧

- Java 8
- Spring Boot 2.7.18
- Spring Security
- Spring Data JPA
- PostgreSQL 42.7.4
- Docker & Docker Compose
- Swagger UI 3.0

### 相關專案

- ad-midd：AD 中台系統，負責 AD 相關操作
- ee-midd：本專案，員工中台系統

## 必要條件

- Java 8 或以上
- Docker Engine
- Docker Compose
- PostgreSQL 42.7.4
- 4GB 以上記憶體
- 50GB 以上硬碟空間

## 安裝步驟

1. 複製專案
```bash
git clone https://github.com/your-organization/ee-midd.git
cd ee-midd
```

2. 配置環境變數
```bash
cp .env.example .env
# 編輯 .env 檔案，設定必要的環境變數
```

3. 建立資料庫
```bash
# 使用提供的 SQL 腳本建立資料庫結構
psql -U postgres -d ee_midd -f sql/schema.sql
```

4. 啟動服務
```bash
docker-compose up -d
```

5. 驗證安裝
```bash
# 檢查服務狀態
docker-compose ps

# 檢查日誌
docker-compose logs -f ee-midd
```

## 配置說明

### 環境變數

必要的環境變數配置：

```properties
# 應用程式設定
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod

# 資料庫配置
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/ee_midd
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password

# Radar API 配置
RADAR_API_URL=https://uat-ehr.sogo.com.tw/DATAHUB
RADAR_API_TOKEN=your_token

# 日誌配置
LOGGING_FILE_PATH=/app/logs
LOGGING_FILE_NAME=ee-midd.log
```

### 資料庫配置

PostgreSQL 配置要求：

```yaml
版本: 42.7.4
端口: 5433
資料庫名: ee_midd
字元集: UTF-8
時區: Asia/Taipei
```

## API 文件

- Swagger UI：http://localhost:8080/swagger-ui.html
- OpenAPI 規格：http://localhost:8080/v3/api-docs

## 開發指南

### 程式碼規範

- 遵循 Google Java Style Guide
- 使用 Lombok 簡化程式碼
- 確保適當的單元測試覆蓋率
- 遵循 RESTful API 設計規範

### 分支管理

- main：主要分支，穩定版本
- develop：開發分支
- feature/*：新功能分支
- hotfix/*：緊急修復分支

## 監控與維護

### 健康檢查

- 健康檢查端點：http://localhost:8080/actuator/health
- 監控端點：http://localhost:8080/actuator/metrics

### 日誌管理

- 位置：/app/logs/ee-midd.log
- 日誌級別：
  - ROOT: INFO
  - 應用: DEBUG
  - Spring: DEBUG

## 貢獻指南

1. Fork 本專案
2. 建立功能分支
3. 提交變更
4. 發送 Pull Request

## 授權資訊

本專案為 SOGO 專有軟體，僅供內部使用。

## 支援與協助

如有任何問題，請聯繫：

- 技術支援：[IT 部門郵箱]
- 專案負責人：[負責人郵箱]

## 環境變數配置說明

### 必要環境變數

```properties
# 應用程式基本設定
SERVER_PORT=8080                           # 應用程式埠號
SPRING_PROFILES_ACTIVE=prod                # 執行環境（dev/prod）
APP_ENCODING=UTF-8                         # 字元編碼
APP_TIMEZONE=Asia/Taipei                   # 時區設定

# 資料庫配置
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/ee_midd    # 資料庫連線 URL
SPRING_DATASOURCE_USERNAME=postgres        # 資料庫使用者名稱
SPRING_DATASOURCE_PASSWORD=your_password   # 資料庫密碼
SPRING_JPA_HIBERNATE_DDL_AUTO=none        # Hibernate DDL 模式
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect  # 資料庫方言

# Radar API 配置
RADAR_API_URL=https://uat-ehr.sogo.com.tw/DATAHUB  # Radar API 基礎 URL
RADAR_API_TOKEN=your_token                 # API 認證 Token
RADAR_API_TIMEOUT=30000                    # API 超時設定（毫秒）
RADAR_API_MAX_RETRY=3                      # API 重試次數

# 日誌配置
LOGGING_FILE_PATH=/app/logs               # 日誌檔案路徑
LOGGING_FILE_NAME=ee-midd.log            # 日誌檔案名稱
LOGGING_LEVEL_ROOT=INFO                   # 根日誌級別
LOGGING_LEVEL_COM_SOGO=DEBUG             # 應用程式日誌級別
LOGGING_FILE_MAX_SIZE=10MB               # 單一日誌檔案大小上限
LOGGING_FILE_MAX_HISTORY=5               # 保留歷史日誌檔案數量

# 快取配置
SPRING_CACHE_TYPE=caffeine               # 快取類型
SPRING_CACHE_CAFFEINE_SPEC=maximumSize=500,expireAfterWrite=3600s  # 快取策略

# 監控配置
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,metrics,info  # 開放監控端點
MANAGEMENT_ENDPOINT_HEALTH_SHOW_DETAILS=always                # 健康檢查詳細資訊

# Swagger 文件配置
SPRINGDOC_SWAGGER_UI_PATH=/swagger-ui.html  # Swagger UI 路徑
SPRINGDOC_API_DOCS_PATH=/v3/api-docs       # OpenAPI 文件路徑
```

### 選用環境變數

```properties
# 效能調校
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=10     # 資料庫連線池大小
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=5           # 最小閒置連線數
SPRING_DATASOURCE_HIKARI_IDLE_TIMEOUT=300000      # 閒置連線超時時間

# 同步作業配置
SYNC_BATCH_SIZE=1000                      # 批次處理大小
SYNC_INITIAL_DELAY=60000                  # 初始延遲時間（毫秒）
SYNC_FIXED_RATE=3600000                   # 固定執行間隔（毫秒）

# 安全性配置
SPRING_SECURITY_USER_NAME=admin           # 管理介面使用者名稱
SPRING_SECURITY_USER_PASSWORD=admin       # 管理介面密碼
```

### 環境變數設定方式

1. 開發環境
   ```bash
   # 複製範例檔案
   cp .env.example .env
   
   # 編輯 .env 檔案
   vim .env
   ```

2. 生產環境
   ```bash
   # 使用 Docker Compose 環境變數檔案
   docker-compose --env-file /path/to/prod.env up -d
   ```

3. 容器執行時覆蓋
   ```bash
   docker-compose up -d --env-file /path/to/custom.env
   ```
