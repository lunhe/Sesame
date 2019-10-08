##mybatis label

###key words

```
SqlSessionFactoryBuilder
SqlSessionFactory
```

核心配置文件（mybatis-config.xml）

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">		校验头
<configuration>											配置标签
  <environments default="development">					定义一组环境
    <environment id="development">						定义一个环境
      <transactionManager type="JDBC"/>					事务管理类型
      <dataSource type="POOLED">						指定数据源，是否开启连接池
        <property name="driver" value="${driver}"/>
        <property name="url" value="${url}"/>
        <property name="username" value="${username}"/>
        <property name="password" value="${password}"/>
      </dataSource>
    </environment>
  </environments>
  <mappers>												mapperXml的加载路径
    <mapper resource="org/mybatis/example/BlogMapper.xml"/>
  </mappers>
</configuration>
```

默认事务级别

```
可重复读，标准上应该是加锁实现，防止幻读

但mysql使用了mvcc的实现，可以实现在可重复读的事务级别下读写互不影响（这个操作是为啥，好神奇）

DDL（表结构之类的修改操作）的修改操作会锁住全表，导致后面来的任何请求都进入等待状态
```
