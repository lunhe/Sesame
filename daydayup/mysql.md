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

innodb与myisam
--
[详细介绍](https://www.cnblogs.com/y-rong/p/8110596.html)

关键字提示
myisam:

```
默认类型   适合执行大量select  没有事务  不支持外键    表级锁（所有操作直接锁表）  
堆表（三个文件，表名开头 ，frm存放表结构，MYD存放数据，MYI存放索引）  
静态表  动态表  压缩表  
自增字段必须为索引  组合索引可以不在第一列   允许存在无索引无主键表    存
有总行数记录，count效率高   支持FULLTEXT的全文索引（？？）  
```
innodb:

```
适合并发  insert  update  delete 支持事务  支持外键  支持行锁（模糊的sql会退化为表锁）
索引组织表   共享表空间   多表空间存储  frm文件存储表结构   索引和数据存储在一起  idb文件   
自增字段必须是索引   组合索引时自增必须在第一列
如果没有创建主键或唯一非空索引  会自动生成6字节的主键（用户不可见）
不记录总行数  count消耗很大  不支持FULLTEXT的全文索引，可以使用sphinx插件支持（？？）
```

innodb事务：

```
事务隔离级别：未提交读(Read uncommitted)，已提交读(Read committed)，可重复读(Repeatable read)，可序列化(Serializable)


Innodb的行锁模式有以下几种：共享锁，排他锁，意向共享锁(表锁)，意向排他锁(表锁)，间隙锁。

注意：当语句没有使用索引，innodb不能确定操作的行，这个时候就使用的意向锁，也就是表锁
```

开发的注意事项：

```
1、可以用 show create table tablename 命令看表的引擎类型。

2、对不支持事务的表做start/commit操作没有任何效果，在执行commit前已经提交。

3、可以执行以下命令来切换非事务表到事务（数据不会丢失），innodb表比myisam表更安全：alter table tablename type=innodb;或者使用 alter table tablename engine = innodb;

4、默认innodb是开启自动提交的，如果你按照myisam的使用方法来编写代码页不会存在错误，只是性能会很低。

如何在编写代码时候提高数据库性能：

	a、尽量将多个语句绑到一个事务中，进行提交，避免多次提交导致的数据库开销。

	b、在一个事务获得排他锁或者意向排他锁以后，如果后面还有需要处理的sql语句，在这两条或者多条sql语句之间程序应尽量少的进行逻辑运算和处理，减少锁的时间。

	c、尽量避免死锁

	d、sql语句如果有where子句一定要使用索引，尽量避免获取意向排他锁。(???)

	(???)f、针对我们自己的数据库环境，日志系统是直插入，不修改的，所以我们使用混合引擎方式，ZION_LOG_DB照旧使用myisam存储引擎，只有ZION_GAME_DB，ZION_LOGIN_DB，DAUM_BILLING使用Innodb引擎。
```

dual

```
dual 时oracle中为了满足 select……from……的结构设置的关键字，再mysql和sqlserver中支持select 1 这中无from的语法。注意：select * from dual在mysql中报错，在oracle会返回一行默认的数据
```

```
using BTREE : 使用在索引上，指明索引的数据结构（通常索引为BTREE或者HASH）  BTREE是MYISAM和InnoDB唯一支持的数据结构，MEMORY和HEAP存储引擎可以支持HASH和BTREE索引


```







