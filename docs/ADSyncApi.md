# AD 同步 API 文件

本文件提供 AD 同步 API 的詳細說明，包括所有端點的輸入、輸出規格以及使用範例。

## 基本資訊

- **基礎 URL**: `/api/v1`
- **內容類型**: `application/json`

## API 端點

### 1. 獲取 AD 員工同步數據

獲取需要同步到 Active Directory 的員工數據。

#### 請求

- **方法**: `GET`
- **URL**: `/api/v1/ad-employee-sync-data`
- **參數**:
  - `base-date` (必填): 基準日期，格式為 yyyy-MM-dd，將返回此日期之後的數據

#### 響應

- **成功響應 (200 OK)**:
  ```json
  [
    {
      "employeeNo": "string",
      "action": "string",
      "employeeInfo": {
        "employeeNo": "string",
        "fullName": "string",
        "emailAddress": "string",
        "extNo": "string",
        "isTerminated": "string",
        "hireDate": "date",
        "azureAccount": "string"
      },
      "orgHierarchyDto": [
        {
          "orgCode": "string",
          "orgName": "string",
          "orgLevel": "number"
        }
      ],
      "updatedFields": {
        "field1": "value1",
        "field2": "value2"
      }
    }
  ]
  ```

- **無內容 (204 No Content)**: 未找到 AD 員工同步數據
- **伺服器錯誤 (500 Internal Server Error)**: 處理請求時發生錯誤

#### 欄位說明

| 欄位名稱 | 類型 | 描述 |
|---------|------|------|
| employeeNo | 字串 | 員工編號 |
| action | 字串 | 操作類型 (C: 創建, U: 更新, D: 刪除) |
| employeeInfo | 對象 | 員工基本資訊 |
| employeeInfo.employeeNo | 字串 | 員工編號 |
| employeeInfo.fullName | 字串 | 員工全名 |
| employeeInfo.emailAddress | 字串 | 電子郵件地址 |
| employeeInfo.extNo | 字串 | 分機號碼 |
| employeeInfo.isTerminated | 字串 | 是否離職 (0:在職, 1:離職) |
| employeeInfo.hireDate | 日期 | 入職日期 |
| employeeInfo.azureAccount | 字串 | Azure 帳號 |
| orgHierarchyDto | 數組 | 組織層級資訊 |
| orgHierarchyDto[].orgCode | 字串 | 組織代碼 |
| orgHierarchyDto[].orgName | 字串 | 組織名稱 |
| orgHierarchyDto[].orgLevel | 數字 | 組織層級 |
| updatedFields | 對象 | 更新的欄位 (僅在 action 為 U 時有值) |

#### 樣本數據

```json
[
  {
    "employeeNo": "E001",
    "action": "C",
    "employeeInfo": {
      "employeeNo": "E001",
      "fullName": "張三",
      "emailAddress": "zhang.san@example.com",
      "extNo": "1001",
      "isTerminated": "0",
      "hireDate": "2025-03-01",
      "azureAccount": "zhang.san@azure.example.com"
    },
    "orgHierarchyDto": [
      {
        "orgCode": "D001",
        "orgName": "資訊科技部",
        "orgLevel": 1
      },
      {
        "orgCode": "D004",
        "orgName": "系統開發組",
        "orgLevel": 2
      }
    ],
    "updatedFields": {}
  },
  {
    "employeeNo": "E002",
    "action": "U",
    "employeeInfo": {
      "employeeNo": "E002",
      "fullName": "李四",
      "emailAddress": "li.si@example.com",
      "extNo": "1002",
      "isTerminated": "0",
      "hireDate": "2024-10-15",
      "azureAccount": "li.si@azure.example.com"
    },
    "orgHierarchyDto": [
      {
        "orgCode": "D002",
        "orgName": "人力資源部",
        "orgLevel": 1
      }
    ],
    "updatedFields": {
      "extNo": "1002",
      "emailAddress": "li.si@example.com"
    }
  },
  {
    "employeeNo": "E003",
    "action": "D",
    "employeeInfo": {
      "employeeNo": "E003",
      "fullName": "王五",
      "emailAddress": "wang.wu@example.com",
      "extNo": "1003",
      "isTerminated": "1",
      "hireDate": "2023-05-10",
      "azureAccount": "wang.wu@azure.example.com"
    },
    "orgHierarchyDto": [],
    "updatedFields": {}
  }
]
```

#### Python 範例

```python
import requests
from datetime import datetime

def get_ad_employee_sync_data(base_date):
    url = "http://your-server/api/v1/ad-employee-sync-data"
    
    headers = {
        "Authorization": "Bearer fdjksa;312kOIJFfdsaASDWE123fdsa9012FDSAfdsa890FDSfdsa90FDSALfd890FDSfds8902FDJSL890fdFDSA890fdaFDSA890fdsaFDSA890fdsaFDSA890fdsFDSA890fdsaFDSA890"
    }
    
    params = {
        "base-date": base_date
    }
    
    try:
        response = requests.get(url, params=params, headers=headers)
        
        if response.status_code == 200:
            return response.json()
        elif response.status_code == 204:
            print("未找到 AD 員工同步數據")
            return []
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
base_date = "2025-03-07"
employee_sync_data = get_ad_employee_sync_data(base_date)

if employee_sync_data:
    for data in employee_sync_data:
        action_type = {
            "C": "創建",
            "U": "更新",
            "D": "刪除"
        }.get(data["action"], "未知")
        
        print(f"員工編號: {data['employeeNo']}, 操作: {action_type}")
        
        if data["action"] == "U":
            print(f"更新欄位: {data['updatedFields']}")
        
        if "employeeInfo" in data and data["employeeInfo"]:
            employee = data["employeeInfo"]
            status = "在職" if employee.get("isTerminated") == "0" else "離職"
            print(f"員工資訊: {employee.get('fullName')}, 狀態: {status}")
        
        if "orgHierarchyDto" in data and data["orgHierarchyDto"]:
            print("組織層級:")
            for org in data["orgHierarchyDto"]:
                print(f"  {org.get('orgName')} (層級: {org.get('orgLevel')})")
```

### 2. 獲取 AD 組織同步數據

獲取需要同步到 Active Directory 的組織數據。

#### 請求

- **方法**: `GET`
- **URL**: `/api/v1/ad-organization-sync-data`
- **參數**:
  - `base-date` (必填): 基準日期，格式為 yyyy-MM-dd，將返回此日期之後的數據

#### 響應

- **成功響應 (200 OK)**:
  ```json
  [
    {
      "orgCode": "string",
      "action": "string",
      "organization": {
        "orgCode": "string",
        "orgName": "string",
        "orgShortName": "string",
        "orgEnName": "string",
        "orgEnShortName": "string",
        "orgStatus": "string"
      },
      "orgHierarchyDto": [
        {
          "orgCode": "string",
          "orgName": "string",
          "orgLevel": "number"
        }
      ],
      "updatedFields": {
        "field1": "value1",
        "field2": "value2"
      }
    }
  ]
  ```

- **無內容 (204 No Content)**: 未找到 AD 組織同步數據
- **伺服器錯誤 (500 Internal Server Error)**: 處理請求時發生錯誤

#### 欄位說明

| 欄位名稱 | 類型 | 描述 |
|---------|------|------|
| orgCode | 字串 | 組織代碼 |
| action | 字串 | 操作類型 (C: 創建, U: 更新, D: 刪除) |
| organization | 對象 | 組織基本資訊 |
| organization.orgCode | 字串 | 組織代碼 |
| organization.orgName | 字串 | 組織名稱 |
| organization.orgShortName | 字串 | 組織簡稱 |
| organization.orgEnName | 字串 | 組織英文名稱 |
| organization.orgEnShortName | 字串 | 組織英文簡稱 |
| organization.orgStatus | 字串 | 組織狀態 |
| orgHierarchyDto | 數組 | 組織層級資訊 |
| orgHierarchyDto[].orgCode | 字串 | 組織代碼 |
| orgHierarchyDto[].orgName | 字串 | 組織名稱 |
| orgHierarchyDto[].orgLevel | 數字 | 組織層級 |
| updatedFields | 對象 | 更新的欄位 (僅在 action 為 U 時有值) |

#### 樣本數據

```json
[
  {
    "orgCode": "D006",
    "action": "C",
    "organization": {
      "orgCode": "D006",
      "orgName": "研發部",
      "orgShortName": "研發",
      "orgEnName": "Research and Development Department",
      "orgEnShortName": "R&D Dept",
      "orgStatus": "1"
    },
    "orgHierarchyDto": [
      {
        "orgCode": "ROOT",
        "orgName": "公司",
        "orgLevel": 0
      }
    ],
    "updatedFields": {}
  },
  {
    "orgCode": "D002",
    "action": "U",
    "organization": {
      "orgCode": "D002",
      "orgName": "人力資源部",
      "orgShortName": "HR部",
      "orgEnName": "Human Resources Department",
      "orgEnShortName": "HR Dept",
      "orgStatus": "1"
    },
    "orgHierarchyDto": [
      {
        "orgCode": "ROOT",
        "orgName": "公司",
        "orgLevel": 0
      }
    ],
    "updatedFields": {
      "orgEnName": "Human Resources Department",
      "orgEnShortName": "HR Dept"
    }
  },
  {
    "orgCode": "D005",
    "action": "D",
    "organization": {
      "orgCode": "D005",
      "orgName": "客服部",
      "orgShortName": "客服",
      "orgEnName": "Customer Service Department",
      "orgEnShortName": "CS Dept",
      "orgStatus": "0"
    },
    "orgHierarchyDto": [],
    "updatedFields": {}
  }
]
```

#### Python 範例

```python
import requests
from datetime import datetime

def get_ad_organization_sync_data(base_date):
    url = "http://your-server/api/v1/ad-organization-sync-data"
    
    headers = {
        "Authorization": "Bearer fdjksa;312kOIJFfdsaASDWE123fdsa9012FDSAfdsa890FDSfdsa90FDSALfd890FDSfds8902FDJSL890fdFDSA890fdaFDSA890fdsaFDSA890fdsaFDSA890fdsFDSA890fdsaFDSA890"
    }
    
    params = {
        "base-date": base_date
    }
    
    try:
        response = requests.get(url, params=params, headers=headers)
        
        if response.status_code == 200:
            return response.json()
        elif response.status_code == 204:
            print("未找到 AD 組織同步數據")
            return []
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
base_date = "2025-03-07"
organization_sync_data = get_ad_organization_sync_data(base_date)

if organization_sync_data:
    for data in organization_sync_data:
        action_type = {
            "C": "創建",
            "U": "更新",
            "D": "刪除"
        }.get(data["action"], "未知")
        
        print(f"組織代碼: {data['orgCode']}, 操作: {action_type}")
        
        if data["action"] == "U":
            print(f"更新欄位: {data['updatedFields']}")
        
        if "organization" in data and data["organization"]:
            org = data["organization"]
            print(f"組織資訊: {org.get('orgName')} ({org.get('orgShortName')})")
        
        if "orgHierarchyDto" in data and data["orgHierarchyDto"]:
            print("組織層級:")
            for org in data["orgHierarchyDto"]:
                print(f"  {org.get('orgName')} (層級: {org.get('orgLevel')})")
``` 