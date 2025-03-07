# ee-midd

初始化系統

1. 初始化系統

日後同步順序

1. 同步公司 - truncate load
2. 同步組織主管 - truncate load
3. 同步組織樹 - truncate load
4. 同步組織 (不處裡刪除組織, 由員工半年 review AD) - 比對後存 action log
5. 同步員工 (同步員工時, 相當於處理組織異動) - 比對後存 action log
