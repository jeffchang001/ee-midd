# BPM API 文件

本文件提供 BPM API 的詳細說明，包括所有端點的輸入、輸出規格以及使用範例。

## 基本資訊

- **基礎 URL**: `/api/v1/bpm`
- **內容類型**: `application/json`

## API 端點

### 1. 獲取所有部門等級資訊

獲取系統中所有部門等級的相關資訊。

#### 請求

- **方法**: `GET`
- **URL**: `/api/v1/bpm/dept-grade-info`
- **參數**: 無

#### 響應

- **成功響應 (200 OK)**:

  ```json
  [
    {
      "deptCode": "string",
      "deptName": "string",
      "deptGrade": "string",
      "deptLevel": "number"
    }
  ]
  ```
- **無內容 (204 No Content)**: 未找到部門等級資訊
- **伺服器錯誤 (500 Internal Server Error)**: 處理請求時發生錯誤

#### 欄位說明

| 欄位名稱  | 類型 | 描述     |
| --------- | ---- | -------- |
| deptCode  | 字串 | 部門代碼 |
| deptName  | 字串 | 部門名稱 |
| deptGrade | 字串 | 部門等級 |
| deptLevel | 數字 | 部門層級 |

#### 樣本數據

```json
[
  {
    "deptCode": "D001",
    "deptName": "資訊科技部",
    "deptGrade": "A",
    "deptLevel": 1
  },
  {
    "deptCode": "D002",
    "deptName": "人力資源部",
    "deptGrade": "A",
    "deptLevel": 1
  },
  {
    "deptCode": "D003",
    "deptName": "財務部",
    "deptGrade": "B",
    "deptLevel": 2
  },
  {
    "deptCode": "D004",
    "deptName": "行銷部",
    "deptGrade": "B",
    "deptLevel": 2
  }
]
```

#### Python 範例

```python
import requests

def get_all_dept_grade_info():
    url = "http://your-server/api/v1/bpm/dept-grade-info"
  
    headers = {
        "Authorization": "Bearer fdjksa;312kOIJFfdsaASDWE123fdsa9012FDSAfdsa890FDSfdsa90FDSALfd890FDSfds8902FDJSL890fdFDSA890fdaFDSA890fdsaFDSA890fdsaFDSA890fdsFDSA890fdsaFDSA890"
    }
  
    try:
        response = requests.get(url, headers=headers)
      
        if response.status_code == 200:
            return response.json()
        elif response.status_code == 204:
            print("未找到部門等級資訊")
            return []
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
dept_grade_info = get_all_dept_grade_info()
if dept_grade_info:
    for dept in dept_grade_info:
        print(f"部門: {dept['deptName']}, 等級: {dept['deptGrade']}")
```

### 2. 獲取所有部門資訊

獲取系統中所有部門的基本資訊。

#### 請求

- **方法**: `GET`
- **URL**: `/api/v1/bpm/dept-info`
- **參數**: 無

#### 響應

- **成功響應 (200 OK)**:

  ```json
  [
    {
      "deptCode": "string",
      "deptName": "string",
      "deptShortName": "string",
      "deptEnName": "string",
      "deptEnShortName": "string",
      "deptStatus": "string"
    }
  ]
  ```
- **無內容 (204 No Content)**: 未找到部門資訊
- **伺服器錯誤 (500 Internal Server Error)**: 處理請求時發生錯誤

#### 欄位說明

| 欄位名稱        | 類型 | 描述         |
| --------------- | ---- | ------------ |
| deptCode        | 字串 | 部門代碼     |
| deptName        | 字串 | 部門名稱     |
| deptShortName   | 字串 | 部門簡稱     |
| deptEnName      | 字串 | 部門英文名稱 |
| deptEnShortName | 字串 | 部門英文簡稱 |
| deptStatus      | 字串 | 部門狀態     |

#### 樣本數據

```json
[
  {
    "deptCode": "D001",
    "deptName": "資訊科技部",
    "deptShortName": "IT部",
    "deptEnName": "Information Technology Department",
    "deptEnShortName": "IT Dept",
    "deptStatus": "1"
  },
  {
    "deptCode": "D002",
    "deptName": "人力資源部",
    "deptShortName": "HR部",
    "deptEnName": "Human Resources Department",
    "deptEnShortName": "HR Dept",
    "deptStatus": "1"
  },
  {
    "deptCode": "D003",
    "deptName": "財務部",
    "deptShortName": "財務部",
    "deptEnName": "Finance Department",
    "deptEnShortName": "Finance Dept",
    "deptStatus": "1"
  }
]
```

#### Python 範例

```python
import requests

def get_all_dept_info():
    url = "http://your-server/api/v1/bpm/dept-info"
  
    headers = {
        "Authorization": "Bearer fdjksa;312kOIJFfdsaASDWE123fdsa9012FDSAfdsa890FDSfdsa90FDSALfd890FDSfds8902FDJSL890fdFDSA890fdaFDSA890fdsaFDSA890fdsaFDSA890fdsFDSA890fdsaFDSA890"
    }
  
    try:
        response = requests.get(url, headers=headers)
      
        if response.status_code == 200:
            return response.json()
        elif response.status_code == 204:
            print("未找到部門資訊")
            return []
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
dept_info = get_all_dept_info()
if dept_info:
    for dept in dept_info:
        print(f"部門: {dept['deptName']} ({dept['deptCode']})")
```

### 3. 獲取所有部門結構資訊

獲取系統中所有部門的結構關係資訊。

#### 請求

- **方法**: `GET`
- **URL**: `/ee-midd/api/v1/bpm/dept-struct`
- **參數**: 無

#### 響應

- **成功響應 (200 OK)**:

  ```json
  [
    {
      "orgCode": "string",
      "parentOrgCode": "string",
      "gradeId": "string",
      "gradeNum": "string",
      "topOrgtreeCode": "string"
    }
  ]
  ```
- **無內容 (204 No Content)**: 未找到部門結構資訊
- **伺服器錯誤 (500 Internal Server Error)**: 處理請求時發生錯誤

#### 欄位說明

| 欄位名稱       | 類型 | 描述           |
| -------------- | ---- | -------------- |
| orgCode        | 字串 | 組織代碼       |
| parentOrgCode  | 字串 | 父組織代碼     |
| gradeId        | 字串 | 等級 ID        |
| gradeNum       | 字串 | 等級數值       |
| topOrgtreeCode | 字串 | 頂層組織樹代碼 |

#### 樣本數據

```json
[
  {
    "orgCode": "100110011",
    "parentOrgCode": null,
    "gradeId": "Level1",
    "gradeNum": "-1",
    "topOrgtreeCode": "100110011"
  },
  {
    "orgCode": "100111000",
    "parentOrgCode": "100110011",
    "gradeId": "Level2",
    "gradeNum": "-2",
    "topOrgtreeCode": "100110011"
  },
  {
    "orgCode": "100111100",
    "parentOrgCode": "100111000",
    "gradeId": "Level3",
    "gradeNum": "-3",
    "topOrgtreeCode": "100110011"
  },
  {
    "orgCode": "100111110",
    "parentOrgCode": "100111100",
    "gradeId": "Level4",
    "gradeNum": "-4",
    "topOrgtreeCode": "100110011"
  },
  {
    "orgCode": "100111120",
    "parentOrgCode": "100111100",
    "gradeId": "Level4",
    "gradeNum": "-4",
    "topOrgtreeCode": "100110011"
  }
]
```

#### Python 範例

```python
import requests

def get_all_dept_struct():
    url = "http://your-server/ee-midd/api/v1/bpm/dept-struct"
  
    headers = {
        "Authorization": "Bearer fdjksa;312kOIJFfdsaASDWE123fdsa9012FDSAfdsa890FDSfdsa90FDSALfd890FDSfds8902FDJSL890fdFDSA890fdaFDSA890fdsaFDSA890fdsaFDSA890fdsFDSA890fdsaFDSA890"
    }
  
    try:
        response = requests.get(url, headers=headers)
      
        if response.status_code == 200:
            return response.json()
        elif response.status_code == 204:
            print("未找到部門結構資訊")
            return []
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
dept_struct = get_all_dept_struct()
if dept_struct:
    for struct in dept_struct:
        parent = struct['parentOrgCode'] if struct['parentOrgCode'] else "無"
        print(f"組織: {struct['orgCode']}, 父組織: {parent}, 等級: {struct['gradeId']}")
```

### 4. 獲取所有職稱對應等級資訊

獲取系統中所有職稱與等級的對應關係。

#### 請求

- **方法**: `GET`
- **URL**: `/api/v1/bpm/job-title-grade`
- **參數**: 無

#### 響應

- **成功響應 (200 OK)**:

  ```json
  [
    {
      "jobTitle": "string",
      "jobGrade": "string"
    }
  ]
  ```
- **無內容 (204 No Content)**: 未找到職稱對應等級資訊
- **伺服器錯誤 (500 Internal Server Error)**: 處理請求時發生錯誤

#### 欄位說明

| 欄位名稱 | 類型 | 描述           |
| -------- | ---- | -------------- |
| jobTitle | 字串 | 職稱           |
| jobGrade | 字串 | 職稱對應的等級 |

#### 樣本數據

```json
[
  {
    "jobTitle": "總經理",
    "jobGrade": "S1"
  },
  {
    "jobTitle": "副總經理",
    "jobGrade": "S2"
  },
  {
    "jobTitle": "協理",
    "jobGrade": "M1"
  },
  {
    "jobTitle": "經理",
    "jobGrade": "M2"
  },
  {
    "jobTitle": "副理",
    "jobGrade": "M3"
  },
  {
    "jobTitle": "主任",
    "jobGrade": "E1"
  },
  {
    "jobTitle": "專員",
    "jobGrade": "E2"
  },
  {
    "jobTitle": "助理",
    "jobGrade": "E3"
  }
]
```

#### Python 範例

```python
import requests

def get_all_job_title_grade():
    url = "http://your-server/api/v1/bpm/job-title-grade"
  
    headers = {
        "Authorization": "Bearer fdjksa;312kOIJFfdsaASDWE123fdsa9012FDSAfdsa890FDSfdsa90FDSALfd890FDSfds8902FDJSL890fdFDSA890fdaFDSA890fdsaFDSA890fdsaFDSA890fdsFDSA890fdsaFDSA890"
    }
  
    try:
        response = requests.get(url, headers=headers)
      
        if response.status_code == 200:
            return response.json()
        elif response.status_code == 204:
            print("未找到職稱對應等級資訊")
            return []
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
job_title_grade = get_all_job_title_grade()
if job_title_grade:
    for job in job_title_grade:
        print(f"職稱: {job['jobTitle']}, 等級: {job['jobGrade']}")
```

### 5. 獲取所有成員資訊

獲取系統中所有成員的基本資訊。

#### 請求

- **方法**: `GET`
- **URL**: `/api/v1/bpm/member-info`
- **參數**: 無

#### 響應

- **成功響應 (200 OK)**:

  ```json
  [
    {
      "employeeNo": "string",
      "fullName": "string",
      "extNo": "string",
      "emailAddress": "string",
      "isterminated": "string",
      "hireDate": "yyyy-MM-dd",
      "azureAccount": "string"
    }
  ]
  ```
- **無內容 (204 No Content)**: 未找到成員資訊
- **伺服器錯誤 (500 Internal Server Error)**: 處理請求時發生錯誤

#### 欄位說明

| 欄位名稱     | 類型 | 描述                        |
| ------------ | ---- | --------------------------- |
| employeeNo   | 字串 | 員工編號                    |
| fullName     | 字串 | 員工全名                    |
| extNo        | 字串 | 分機號碼                    |
| emailAddress | 字串 | 電子郵件地址                |
| isterminated | 字串 | 是否離職 (0:在職, 1:離職)   |
| hireDate     | 日期 | 入職日期 (格式: yyyy-MM-dd) |
| azureAccount | 字串 | Azure 帳號                  |

#### 樣本數據

```json
[
  {
    "employeeNo": "E001",
    "fullName": "張三",
    "extNo": "1001",
    "emailAddress": "zhang.san@example.com",
    "isterminated": "0",
    "hireDate": "2020-01-15",
    "azureAccount": "zhang.san@azure.example.com"
  },
  {
    "employeeNo": "E002",
    "fullName": "李四",
    "extNo": "1002",
    "emailAddress": "li.si@example.com",
    "isterminated": "0",
    "hireDate": "2020-03-20",
    "azureAccount": "li.si@azure.example.com"
  },
  {
    "employeeNo": "E003",
    "fullName": "王五",
    "extNo": "1003",
    "emailAddress": "wang.wu@example.com",
    "isterminated": "1",
    "hireDate": "2019-05-10",
    "azureAccount": "wang.wu@azure.example.com"
  }
]
```

#### Python 範例

```python
import requests
from datetime import datetime

def get_all_member_info():
    url = "http://your-server/api/v1/bpm/member-info"
  
    headers = {
        "Authorization": "Bearer fdjksa;312kOIJFfdsaASDWE123fdsa9012FDSAfdsa890FDSfdsa90FDSALfd890FDSfds8902FDJSL890fdFDSA890fdaFDSA890fdsaFDSA890fdsaFDSA890fdsFDSA890fdsaFDSA890"
    }
  
    try:
        response = requests.get(url, headers=headers)
      
        if response.status_code == 200:
            return response.json()
        elif response.status_code == 204:
            print("未找到成員資訊")
            return []
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
member_info = get_all_member_info()
if member_info:
    for member in member_info:
        hire_date = datetime.strptime(member['hireDate'], '%Y-%m-%d').date()
        status = "在職" if member['isterminated'] == "0" else "離職"
        print(f"員工: {member['fullName']} ({member['employeeNo']}), 狀態: {status}, 入職日期: {hire_date}")
```

### 6. 獲取所有成員結構資訊

獲取系統中所有成員的組織結構關係。

#### 請求

- **方法**: `GET`
- **URL**: `/api/v1/bpm/member-struct`
- **參數**: 無

#### 響應

- **成功響應 (200 OK)**:

  ```json
  [
    {
      "employeeNo": "string",
      "deptCode": "string",
      "jobTitle": "string",
      "treeType": "string"
    }
  ]
  ```
- **無內容 (204 No Content)**: 未找到成員結構資訊
- **伺服器錯誤 (500 Internal Server Error)**: 處理請求時發生錯誤

#### 欄位說明

| 欄位名稱   | 類型 | 描述         |
| ---------- | ---- | ------------ |
| employeeNo | 字串 | 員工編號     |
| deptCode   | 字串 | 部門代碼     |
| jobTitle   | 字串 | 職稱         |
| treeType   | 字串 | 樹狀結構類型 |

#### 樣本數據

```json
[
  {
    "employeeNo": "E001",
    "deptCode": "D001",
    "jobTitle": "經理",
    "treeType": "0"
  },
  {
    "employeeNo": "E002",
    "deptCode": "D001",
    "jobTitle": "專員",
    "treeType": "0"
  },
  {
    "employeeNo": "E003",
    "deptCode": "D002",
    "jobTitle": "經理",
    "treeType": "0"
  },
  {
    "employeeNo": "E004",
    "deptCode": "D002",
    "jobTitle": "專員",
    "treeType": "0"
  },
  {
    "employeeNo": "E005",
    "deptCode": "D003",
    "jobTitle": "副理",
    "treeType": "0"
  }
]
```

#### Python 範例

```python
import requests

def get_all_member_struct():
    url = "http://your-server/api/v1/bpm/member-struct"
  
    headers = {
        "Authorization": "Bearer fdjksa;312kOIJFfdsaASDWE123fdsa9012FDSAfdsa890FDSfdsa90FDSALfd890FDSfds8902FDJSL890fdFDSA890fdaFDSA890fdsaFDSA890fdsaFDSA890fdsFDSA890fdsaFDSA890"
    }
  
    try:
        response = requests.get(url, headers=headers)
      
        if response.status_code == 200:
            return response.json()
        elif response.status_code == 204:
            print("未找到成員結構資訊")
            return []
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
member_struct = get_all_member_struct()
if member_struct:
    for struct in member_struct:
        print(f"員工編號: {struct['employeeNo']}, 部門: {struct['deptCode']}, 職稱: {struct['jobTitle']}")
```

### 7. 查詢實體化視圖變更資訊

根據日期和視圖名稱查詢實體化視圖的變更資訊。

#### 請求

- **方法**: `GET`
- **URL**: `/api/v1/bpm/view-changes`
- **參數**:
  - `date` (必填): 查詢日期，格式為 yyyy-MM-dd
  - `view_name` (可選): 視圖名稱

#### 響應

- **成功響應 (200 OK)**:

  ```json
  [
    {
      "viewName": "string",
      "refreshedAt": "yyyy-MM-dd HH:mm:ss",
      "diffCount": number,
      "diffDetails": [
        {
          "key": {
            "field1": "value1",
            "field2": "value2"
          },
          "data": {
            "field1": "value1",
            "field2": "value2",
            "field3": "value3"
          },
          "operation": "string"
        }
      ]
    }
  ]
  ```
- **無內容 (204 No Content)**: 未找到實體化視圖變更資訊
- **伺服器錯誤 (500 Internal Server Error)**: 處理請求時發生錯誤

#### 欄位說明

| 欄位名稱                | 類型     | 描述                                 |
| ----------------------- | -------- | ------------------------------------ |
| viewName                | 字串     | 視圖名稱                             |
| refreshedAt             | 日期時間 | 更新時間 (格式: yyyy-MM-dd HH:mm:ss) |
| diffCount               | 數字     | 變更記錄數量                         |
| diffDetails             | 陣列     | 變更詳細資訊陣列                     |
| diffDetails[].key       | 對象     | 變更記錄的主鍵資訊                   |
| diffDetails[].data      | 對象     | 變更記錄的資料內容                   |
| diffDetails[].operation | 字串     | 操作類型 (INSERT, UPDATE, DELETE)    |

#### 樣本數據

```json
[
  {
    "viewName": "fse7en_org_deptinfo",
    "refreshedAt": "2025-03-11 10:15:30",
    "diffCount": 2,
    "diffDetails": [
      {
        "key": {
          "deptCode": "D006"
        },
        "data": {
          "deptCode": "D006",
          "deptName": "研發部",
          "deptShortName": "研發",
          "deptEnName": "Research and Development Department",
          "deptEnShortName": "R&D Dept",
          "deptStatus": "1"
        },
        "operation": "INSERT"
      },
      {
        "key": {
          "deptCode": "D002"
        },
        "data": {
          "deptCode": "D002",
          "deptName": "人力資源部",
          "deptShortName": "HR部",
          "deptEnName": "Human Resources Department",
          "deptEnShortName": "HR Dept",
          "deptStatus": "0"
        },
        "operation": "UPDATE"
      }
    ]
  },
  {
    "viewName": "fse7en_org_memberinfo",
    "refreshedAt": "2025-03-11 10:20:45",
    "diffCount": 1,
    "diffDetails": [
      {
        "key": {
          "employeeNo": "E010"
        },
        "data": {
          "employeeNo": "E010",
          "fullName": "趙六",
          "extNo": "1010",
          "emailAddress": "zhao.liu@example.com",
          "isterminated": "0",
          "hireDate": "2025-03-10",
          "azureAccount": "zhao.liu@azure.example.com"
        },
        "operation": "INSERT"
      }
    ]
  }
]
```

#### Python 範例

```python
import requests
from datetime import datetime

def get_view_changes_by_date(date, view_name=None):
    url = "http://your-server/api/v1/bpm/view-changes"
  
    headers = {
        "Authorization": "Bearer fdjksa;312kOIJFfdsaASDWE123fdsa9012FDSAfdsa890FDSfdsa90FDSALfd890FDSfds8902FDJSL890fdFDSA890fdaFDSA890fdsaFDSA890fdsaFDSA890fdsFDSA890fdsaFDSA890"
    }
  
    params = {
        "date": date
    }
  
    if view_name:
        params["view_name"] = view_name
  
    try:
        response = requests.get(url, params=params, headers=headers)
      
        if response.status_code == 200:
            return response.json()
        elif response.status_code == 204:
            print("未找到實體化視圖變更資訊")
            return []
        else:
            print(f"錯誤: {response.status_code}, {response.text}")
            return None
    except Exception as e:
        print(f"請求異常: {str(e)}")
        return None

# 使用範例
# 查詢特定日期的所有視圖變更
all_changes = get_view_changes_by_date("2025-03-11")
if all_changes:
    for change in all_changes:
        print(f"視圖: {change['viewName']}, 更新時間: {change['refreshedAt']}, 變更數量: {change['diffCount']}")

# 查詢特定日期和視圖的變更
specific_changes = get_view_changes_by_date("2025-03-11", "fse7en_org_memberstruct")
if specific_changes:
    for change in specific_changes:
        print(f"視圖: {change['viewName']}, 更新時間: {change['refreshedAt']}, 變更數量: {change['diffCount']}")
      
        # 處理變更詳細資訊
        if change['diffDetails'] and len(change['diffDetails']) > 0:
            for diff in change['diffDetails'][:5]:  # 只顯示前5筆變更
                print(f"操作類型: {diff['operation']}")
                print(f"主鍵: {diff['key']}")
                print(f"資料: {diff['data']}")
                print("---") 
```
