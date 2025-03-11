# Radar 數據同步 API 文件

本文件提供 Radar 數據同步 API 的詳細說明，包括所有端點的輸入、輸出規格以及使用範例。

## 基本資訊

- **內容類型**: `application/json`

## API 端點

### 1. 初始化同步公司資訊

從 Radar API 同步公司資訊。

#### 請求

- **方法**: `POST`
- **URL**: `/companies/initial-sync`
- **參數**: 無

#### 響應

- **成功響應 (200 OK)**: 公司資訊同步成功
- **錯誤響應 (400 Bad Request)**: 公司資訊同步失敗
- **伺服器錯誤 (500 Internal Server Error)**: 內部伺服器錯誤

#### Python 範例

```python
import requests

def initial_sync_company():
    url = "http://your-server/companies/initial-sync"
    
    try:
        response = requests.post(url)
        
        if response.status_code == 200:
            print("公司資訊同步成功")
            return response.text
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
result = initial_sync_company()
if result:
    print(f"同步結果: {result}")
```

### 2. 初始化同步組織資訊

從 Radar API 同步組織資訊。

#### 請求

- **方法**: `POST`
- **URL**: `/organizations/initial-sync`
- **參數**:
  - `org-code` (可選): 組織代碼，如果提供則只同步指定組織
  - `base-date` (可選): 基準日期，格式為 yyyy-MM-dd，將同步此日期之後的數據

#### 響應

- **成功響應 (200 OK)**: 組織資訊同步成功
- **錯誤響應 (400 Bad Request)**: 組織資訊同步失敗
- **伺服器錯誤 (500 Internal Server Error)**: 內部伺服器錯誤

#### Python 範例

```python
import requests
from datetime import datetime

def initial_sync_organization(org_code=None, base_date=None):
    url = "http://your-server/organizations/initial-sync"
    
    params = {}
    if org_code:
        params["org-code"] = org_code
    if base_date:
        params["base-date"] = base_date
    
    try:
        response = requests.post(url, params=params)
        
        if response.status_code == 200:
            print("組織資訊同步成功")
            return response.text
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
# 同步所有組織資訊
result1 = initial_sync_organization()

# 同步特定組織資訊
result2 = initial_sync_organization(org_code="ORG001")

# 同步特定日期後的組織資訊
result3 = initial_sync_organization(base_date="2025-03-07")

# 同步特定組織和日期的資訊
result4 = initial_sync_organization(org_code="ORG001", base_date="2025-03-07")
```

### 3. 初始化同步組織管理者資訊

從 Radar API 同步組織管理者資訊。

#### 請求

- **方法**: `POST`
- **URL**: `/organization-managers/initial-sync`
- **參數**:
  - `org-code` (可選): 組織代碼，如果提供則只同步指定組織的管理者
  - `base-date` (可選): 基準日期，格式為 yyyy-MM-dd，將同步此日期之後的數據

#### 響應

- **成功響應 (200 OK)**: 組織管理者資訊同步成功
- **錯誤響應 (400 Bad Request)**: 組織管理者資訊同步失敗
- **伺服器錯誤 (500 Internal Server Error)**: 內部伺服器錯誤

#### Python 範例

```python
import requests
from datetime import datetime

def initial_sync_organization_manager(org_code=None, base_date=None):
    url = "http://your-server/organization-managers/initial-sync"
    
    params = {}
    if org_code:
        params["org-code"] = org_code
    if base_date:
        params["base-date"] = base_date
    
    try:
        response = requests.post(url, params=params)
        
        if response.status_code == 200:
            print("組織管理者資訊同步成功")
            return response.text
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
# 同步所有組織管理者資訊
result1 = initial_sync_organization_manager()

# 同步特定組織的管理者資訊
result2 = initial_sync_organization_manager(org_code="ORG001")

# 同步特定日期後的組織管理者資訊
result3 = initial_sync_organization_manager(base_date="2025-03-07")
```

### 4. 初始化同步組織關係

從 Radar API 同步組織關係。

#### 請求

- **方法**: `POST`
- **URL**: `/organization-relations/initial-sync`
- **參數**:
  - `tree-type` (可選): 樹狀結構類型

#### 響應

- **成功響應 (200 OK)**: 組織關係同步成功
- **錯誤響應 (400 Bad Request)**: 組織關係同步失敗
- **伺服器錯誤 (500 Internal Server Error)**: 內部伺服器錯誤

#### Python 範例

```python
import requests

def initial_sync_organization_relation(tree_type=None):
    url = "http://your-server/organization-relations/initial-sync"
    
    params = {}
    if tree_type:
        params["tree-type"] = tree_type
    
    try:
        response = requests.post(url, params=params)
        
        if response.status_code == 200:
            print("組織關係同步成功")
            return response.text
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
# 同步所有組織關係
result1 = initial_sync_organization_relation()

# 同步特定樹狀結構類型的組織關係
result2 = initial_sync_organization_relation(tree_type="0")
```

### 5. 初始化同步員工資訊

從 Radar API 同步員工資訊。

#### 請求

- **方法**: `POST`
- **URL**: `/employees/initial-sync`
- **參數**:
  - `employee-no` (可選): 員工編號，如果提供則只同步指定員工
  - `base-date` (可選): 基準日期，格式為 yyyy-MM-dd，將同步此日期之後的數據

#### 響應

- **成功響應 (200 OK)**: 員工資訊同步成功
- **錯誤響應 (400 Bad Request)**: 員工資訊同步失敗
- **伺服器錯誤 (500 Internal Server Error)**: 內部伺服器錯誤

#### Python 範例

```python
import requests
from datetime import datetime

def initial_sync_employee_info(employee_no=None, base_date=None):
    url = "http://your-server/employees/initial-sync"
    
    params = {}
    if employee_no:
        params["employee-no"] = employee_no
    if base_date:
        params["base-date"] = base_date
    
    try:
        response = requests.post(url, params=params)
        
        if response.status_code == 200:
            print("員工資訊同步成功")
            return response.text
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
# 同步所有員工資訊
result1 = initial_sync_employee_info()

# 同步特定員工資訊
result2 = initial_sync_employee_info(employee_no="EMP001")

# 同步特定日期後的員工資訊
result3 = initial_sync_employee_info(base_date="2025-03-07")
```

### 6. 獲取組織關係

獲取指定樹狀結構類型的組織關係。

#### 請求

- **方法**: `GET`
- **URL**: `/organization-relations/fetch`
- **參數**:
  - `tree-type` (可選): 樹狀結構類型

#### 響應

- **成功響應 (200 OK)**:
  ```json
  [
    {
      "orgCode": "string",
      "orgName": "string",
      "parentOrgCode": "string",
      "orgLevel": "number",
      "employeeInfoList": [
        {
          "employeeNo": "string",
          "fullName": "string",
          "emailAddress": "string",
          "extNo": "string",
          "isTerminated": "string",
          "hireDate": "date",
          "azureAccount": "string"
        }
      ]
    }
  ]
  ```

- **伺服器錯誤 (500 Internal Server Error)**: 內部伺服器錯誤

#### 欄位說明

| 欄位名稱 | 類型 | 描述 |
|---------|------|------|
| orgCode | 字串 | 組織代碼 |
| orgName | 字串 | 組織名稱 |
| parentOrgCode | 字串 | 父組織代碼 |
| orgLevel | 數字 | 組織層級 |
| employeeInfoList | 數組 | 組織內的員工列表 |
| employeeInfoList[].employeeNo | 字串 | 員工編號 |
| employeeInfoList[].fullName | 字串 | 員工全名 |
| employeeInfoList[].emailAddress | 字串 | 電子郵件地址 |
| employeeInfoList[].extNo | 字串 | 分機號碼 |
| employeeInfoList[].isTerminated | 字串 | 是否離職 (0:在職, 1:離職) |
| employeeInfoList[].hireDate | 日期 | 入職日期 |
| employeeInfoList[].azureAccount | 字串 | Azure 帳號 |

#### Python 範例

```python
import requests

def fetch_organization_relations(tree_type=None):
    url = "http://your-server/organization-relations/fetch"
    
    params = {}
    if tree_type:
        params["tree-type"] = tree_type
    
    try:
        response = requests.get(url, params=params)
        
        if response.status_code == 200:
            return response.json()
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
# 獲取所有組織關係
org_relations = fetch_organization_relations()

# 獲取特定樹狀結構類型的組織關係
org_relations_type0 = fetch_organization_relations(tree_type="0")

if org_relations:
    for org in org_relations:
        print(f"組織: {org['orgName']} ({org['orgCode']}), 層級: {org['orgLevel']}")
        
        if "employeeInfoList" in org and org["employeeInfoList"]:
            print("員工列表:")
            for employee in org["employeeInfoList"]:
                status = "在職" if employee.get("isTerminated") == "0" else "離職"
                print(f"  {employee.get('fullName')} ({employee.get('employeeNo')}), 狀態: {status}")
```

### 7. 檢查是否為組織管理者

檢查指定員工是否為指定組織的管理者。

#### 請求

- **方法**: `GET`
- **URL**: `/organization-managers/check`
- **參數**:
  - `employee-no` (必填): 員工編號
  - `org-code` (必填): 組織代碼

#### 響應

- **成功響應 (200 OK)**: 布爾值，表示是否為組織管理者
- **伺服器錯誤 (500 Internal Server Error)**: 內部伺服器錯誤

#### Python 範例

```python
import requests

def is_org_manager(employee_no, org_code):
    url = "http://your-server/organization-managers/check"
    
    params = {
        "employee-no": employee_no,
        "org-code": org_code
    }
    
    try:
        response = requests.get(url, params=params)
        
        if response.status_code == 200:
            return response.json()
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
employee_no = "EMP001"
org_code = "ORG001"
is_manager = is_org_manager(employee_no, org_code)

if is_manager is not None:
    if is_manager:
        print(f"員工 {employee_no} 是組織 {org_code} 的管理者")
    else:
        print(f"員工 {employee_no} 不是組織 {org_code} 的管理者")
```

### 8. 初始化系統

執行系統初始化，同步所有必要的數據。

#### 請求

- **方法**: `POST`
- **URL**: `/system/initialization`
- **參數**: 無

#### 響應

- **成功響應 (200 OK)**: 系統初始化成功
- **伺服器錯誤 (500 Internal Server Error)**: 系統初始化失敗

#### Python 範例

```python
import requests

def init_database():
    url = "http://your-server/system/initialization"
    
    try:
        response = requests.post(url)
        
        if response.status_code == 200:
            print("系統初始化成功")
            return response.text
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
result = init_database()
if result:
    print(f"初始化結果: {result}")
```

### 9. 取得並比對組織資訊

取得指定日期後的組織資訊，比對後產生同步 AD 資料。

#### 請求

- **方法**: `POST`
- **URL**: `/organizations/fetch-compare`
- **參數**:
  - `org-code` (可選): 組織代碼，如果提供則只處理指定組織
  - `base-date` (必填): 基準日期，格式為 yyyy-MM-dd，將處理此日期之後的數據

#### 響應

- **成功響應 (200 OK)**: 取得並比對組織資訊成功
- **錯誤響應 (400 Bad Request)**: 取得並比對組織資訊失敗
- **伺服器錯誤 (500 Internal Server Error)**: 內部伺服器錯誤

#### Python 範例

```python
import requests
from datetime import datetime

def fetch_compare_organization(base_date, org_code=None):
    url = "http://your-server/organizations/fetch-compare"
    
    params = {
        "base-date": base_date
    }
    
    if org_code:
        params["org-code"] = org_code
    
    try:
        response = requests.post(url, params=params)
        
        if response.status_code == 200:
            print("取得並比對組織資訊成功")
            return response.text
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
# 取得並比對所有組織資訊
result1 = fetch_compare_organization(base_date="2024-09-25")

# 取得並比對特定組織資訊
result2 = fetch_compare_organization(base_date="2024-09-25", org_code="ORG001")
```

### 10. 取得並比對員工資訊

取得指定日期後的員工資訊，比對後產生同步 AD 資料。

#### 請求

- **方法**: `POST`
- **URL**: `/employees/fetch-compare`
- **參數**:
  - `employee-no` (可選): 員工編號，如果提供則只處理指定員工
  - `base-date` (必填): 基準日期，格式為 yyyy-MM-dd，將處理此日期之後的數據

#### 響應

- **成功響應 (200 OK)**: 取得並比對員工資訊成功
- **錯誤響應 (400 Bad Request)**: 取得並比對員工資訊失敗
- **伺服器錯誤 (500 Internal Server Error)**: 內部伺服器錯誤

#### Python 範例

```python
import requests
from datetime import datetime

def fetch_compare_employee_info(base_date, employee_no=None):
    url = "http://your-server/employees/fetch-compare"
    
    params = {
        "base-date": base_date
    }
    
    if employee_no:
        params["employee-no"] = employee_no
    
    try:
        response = requests.post(url, params=params)
        
        if response.status_code == 200:
            print("取得並比對員工資訊成功")
            return response.text
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
# 取得並比對所有員工資訊
result1 = fetch_compare_employee_info(base_date="2024-09-25")

# 取得並比對特定員工資訊
result2 = fetch_compare_employee_info(base_date="2024-09-25", employee_no="EMP001")
```

### 11. 取得沒有員工的組織DN列表

取得組織樹中沒有任何在職員工的組織 DN 列表。

#### 請求

- **方法**: `GET`
- **URL**: `/empty-dns`
- **參數**:
  - `treeType` (可選): 樹狀結構類型，預設為 0

#### 響應

- **成功響應 (200 OK)**:
  ```json
  [
    "string"
  ]
  ```

- **伺服器錯誤 (500 Internal Server Error)**: 伺服器內部錯誤

#### Python 範例

```python
import requests

def get_empty_organization_dns(tree_type=None):
    url = "http://your-server/empty-dns"
    
    params = {}
    if tree_type:
        params["treeType"] = tree_type
    
    try:
        response = requests.get(url, params=params)
        
        if response.status_code == 200:
            return response.json()
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
# 獲取預設樹狀結構類型的空組織 DN 列表
empty_dns = get_empty_organization_dns()

# 獲取特定樹狀結構類型的空組織 DN 列表
empty_dns_type1 = get_empty_organization_dns(tree_type="1")

if empty_dns:
    print("空組織 DN 列表:")
    for dn in empty_dns:
        print(f"  {dn}")
``` 