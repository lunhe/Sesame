###然

nameserver:存储broker的完整路由信息，用于实现服务发现。并支持了这些数据的读写和快速扩展（能够集群化）

broker:存放消息的实际几点，支持push和pull模式。拥有容错机制（2-3备份？？），峰值填充能力，按时间顺序积累大量（上亿数据）消息，灾难恢复，丰富的度量统计，报警机制。

consumer:消息消费者，支持集群，广播，实时消息订阅机制

producer:消息提供者，可以集群化，负载均衡向broker集群发送消息，发送进程能够快速失败（？），延迟低


![](./rmq-basic-arc.png)

###所以然

<b>自解<b/>监听回调机制，解耦。


	场景 ： 一种异步的实现方案：请求集体到达，过滤请求后，将不满足的请求直接拒绝，满足的请求放入队列，后端按照自己的最大能力处理问题，然后返回给消息队列，然后返回给前端。

	1  实现异步处理，消息队列缓存请求，大量的请求处理不会占用服务器资源，造成后端业务阻塞。
	2 解耦，消息队列隔开了请求合法性校验和实际业务逻辑
	3 流量控制，大量的请求进入了消息队列，不会直接冲入后端业务服务器
	4 常见的mq支持广播
	5 分开部署，避免但节点问题。
	
####nameserver

	两个future
	路由管理：整个broker的路由信息和客户端的查询队列
	broker管理：broker注册，心跳
	有四种方法可以配置nameserver的地址到客户端：

	Programmatic Way, like producer.setNamesrvAddr("ip:port").  代码级别  最高优先级
	Java Options, use rocketmq.namesrv.addr.	jvm参数 次优先级
	Environment Variable, use NAMESRV_ADDR.		系统环境 再次优先级
	HTTP Endpoint.								http 最低优先级（被推荐使用这种方式，配置灵活，无需重启）

	



####broker

	Remoting module ：处理来自客户端的请求
	Client manger : 管理所有客户端（consumer，producer） 同时维护客户端的监听主题
	store service ： 提供接口在物理磁盘上存储或者查询消息
	HA module ： 用于主从同步
	Index Service ： 为消息构建特殊的key，提供快速查询 
	
	broker Roles ???
	SYNC_FLUSH
	ASYNC_FLUSH
	SYNC_MASTER
	ASYNC_MASTER 

![](./rmq-basic-component.png)

###Group

	具有相同role的实例，放在一起作为一个组。consumer的group中的所有实例必须要有一致的topic

	
####topic
	
	详细的主题，用于标识消息
	
	
####message

	每个消息必须包含一个主题。另外如果需要还可以附带额外的key
	
####message queue

	Topic is partitioned into one or more sub-topics, “message queues”.
	
####tag

	也即是sub-topic ，额外的消息标记。可以在更丰富的维度上划分消息
	
####message order

	orderly：
	消费顺序等于生产顺序
	If consuming orderly is specified, the maximum concurrency of message consuming is the number of message queues subscribed by the consumer group.
	 
	concurrently：
	以最大的并发量消费message，但消费顺序是无法保证的。

####producer

	producer和broker之间支持三种方式传递message
	同步
	异步
	单向
	
	？？
	Considering the provided producer is sufficiently powerful at sending messages, only one instance is allowed per producer group to avoid unnecessary initialization of producer instances.

	FLUSH_DISK_TIMEOUT  收到来自broker返回的刷新磁盘超时（默认5s）
	FLUSH_SLAVE_TIMEOUT 收到来自broker返回的刷新从服务器超时（默认5s）
	SLAVE_NOT_AVAILABLE  收到来自broker返回的从服务器不可达
	SEND_OK				收到来自broker返回消息发送成功
	发送的消息丢失或者重复  当回到了broker的FLUSH_DISK_TIMEOUT或则FLUSH_SLAVE_TIMEOUT，但此刻broker宕机，那么消息可能会丢失，如果此时重发，则可能造成消息重复（推荐方法：重发+额外的防重复机制）
	RemotingTimeoutException	连接broker超时（默认3s）
	
	推荐消息不超过512k
	默认采用异步发送消息到broker
	
	生产者组：建议一个组一个生产者，通常无需关系组的概念。如果存在一个事务，那么需要注意（也没说咋注意）
	
	If you want more than one producer in one JVM for big data processing, we suggest:
		1 use async sending with a few producers (3~5 is enough)
		2 setInstanceName for each producer
	
	

####consumer
	pull模式：直接到broker拉去message，然后丢到消费任务中消费。
	push模式：框架实现了message的传递，给用户留下了回调接口，用户实现如何消费message。

	consumer instances of a consumer group must have exactly the same topic subscription(s)。

	MessageListener
	Orderly ：顺序消费模式，阻塞消息队列，当消费者被阻塞后，broker可以发送ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT通知。
	
	Concurrently ：并发的消费模式，并发的消费消息，当无法消费时，broker可以发送ConsumeConcurrentlyStatus.RECONSUME_LATER通知。
	
	Blocking：不推荐阻塞消费者MessageListener，这样会阻塞整个消费线程池，造成消费中止。
	
	当一个新的消费组上线后，如果不需要消费broker中已经存在的旧数据，消费起点标识为：Broker.CONSUME_FROM_LAST_OFFSET  ，需要则CONSUME_FROM_FIRST_OFFSET 或者也可以从某个时刻点：
	CONSUME_FROM_TIMESTAMP 。
	
	重复消费：1 生产者重复发送 ， 2 消费者下线的时候没有更新消费点
	
###其他配置

[JVM和OS参数配置](http://rocketmq.apache.org/docs/system-config/)
	










