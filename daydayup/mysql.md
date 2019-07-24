##MySQL
###Notes
一个数据迁移时候的优化方案

```
alter tab1 disable keys  ;  --禁用tab1索引

insert into tab1 select * from tab2 ;  --向tab1迁移数据

alter tab1 enable keys; -- 开启tab1索引
```

executable comments

里面的变量赋值语句是会被执行的。

为什么要把命令写在注释里面呢？ 因为导出脚本有些时候可能会拿到其他数据库上去执行。

40101，是指在MySQL 4.1.1（4.01.01）及以上版本上执行。

```
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
```