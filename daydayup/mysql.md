##MySQL
###Notes
一个数据迁移时候的优化方案

```
alter tab1 disable keys  ;  --禁用tab1索引

insert into tab1 select * from tab2 ;  --向tab1迁移数据

alter tab1 enable keys; -- 开启tab1索引
```