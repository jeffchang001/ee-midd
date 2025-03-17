# EE-MIDD Docker 部署指南

本文檔提供了使用 Docker 和 Docker Compose 部署 EE-MIDD 應用程式的指南。

## 前提條件

- 安裝 [Docker](https://docs.docker.com/get-docker/)
- 安裝 [Docker Compose](https://docs.docker.com/compose/install/)

## 部署步驟

### 1. 配置環境變數

在部署前，您可以修改 `.env` 文件中的環境變數，以適應您的環境需求：

```bash
# 編輯環境變數文件
nano .env
```

主要的環境變數包括：

- 資料庫連接設定
- SMTP 郵件設定
- Radar API 設定
- JWT 密鑰設定

### 2. 構建和啟動容器

使用以下命令構建和啟動應用程式：

```bash
# 構建並啟動容器
docker-compose up -d
```

這將在後台啟動應用程式容器。

### 3. 查看日誌

您可以使用以下命令查看應用程式的日誌：

```bash
# 查看容器日誌
docker-compose logs -f ee-midd
```

### 4. 停止應用程式

使用以下命令停止應用程式：

```bash
# 停止容器
docker-compose down
```

## 訪問應用程式

應用程式啟動後，您可以通過以下 URL 訪問：

- 應用程式: http://localhost:8080/ee-midd
- Swagger UI: http://localhost:8080/ee-midd/swagger-ui.html

## 資料庫連接

此 Docker 配置假設您有一個名為 `postgres` 的 PostgreSQL 容器。如果您需要連接到外部資料庫，請修改 `.env` 文件中的 `SPRING_DATASOURCE_URL` 變數。

## 自定義配置

如果您需要自定義更多設定，可以修改 `docker-compose.yml` 文件中的環境變數。

## 故障排除

### 容器無法啟動

檢查日誌以獲取錯誤信息：

```bash
docker-compose logs ee-midd
```

### 資料庫連接問題

確保資料庫容器已啟動並且可以從 ee-midd 容器訪問：

```bash
# 進入容器
docker-compose exec ee-midd sh

# 測試資料庫連接
ping postgres
```

## 注意事項

- 生產環境中，請確保更改所有預設密碼和敏感設定
- 考慮使用 Docker 卷來持久化資料
- 定期備份您的資料 