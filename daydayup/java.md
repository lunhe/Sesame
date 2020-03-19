###notes
一个进程中，当一个线程oom后，其余线程可以正常执行，因为oom的线程会立马释放所有资源，从而不影响其他线程。
一个进程中，所有线程关系平级，当主线程退出后，子线程除非是守护线程，否则正常执行。

java里头的class文件，头四个字节的Magic Number是多少？
回答："0xCAFEBABE。"这个数字可能比较难记，记(咖啡宝贝)就好。


###CAS(compareAndSwap比较并替换)
乐观锁技术：多个线程同时修改同一个内存值时，只有一个线程可以修改成功，其余线程失败，但所有线程都不会被挂起。
参数 ：参数一  需要修改的内存位置，参数二：需要修改的内存位置的期望值，参数三：新的值。需要使用
流程 ：线程先将参数一的内存值和参数二的期望值比较，如果相等则写入参数三的新值，如果不相等则什么也不做。（类似于乐观锁的 冲突检查+数据更新）
优点：非阻塞，
缺点：

	ABA问题（使用AtomicStampedReference比较引用）
	循环时间长开销大
	只能保证一个共享变量的原子操作
	
CAS与Synchronized的使用情景：　　　

　　　　1、对于资源竞争较少（线程冲突较轻）的情况，使用synchronized同步锁进行线程阻塞和唤醒切换以及用户态内核态间的切换操作额外浪费消耗cpu资源；而CAS基于硬件实现，不需要进入内核，不需要切换线程，操作自旋几率较少，因此可以获得更高的性能。

　　　 2、对于资源竞争严重（线程冲突严重）的情况，CAS自旋的概率会比较大，从而浪费更多的CPU资源，效率低于synchronized。

　　　补充： synchronized在jdk1.6之后，已经改进优化。synchronized的底层实现主要依靠Lock-Free的队列，基本思路是自旋后阻塞，竞争切换后继续竞争锁，稍微牺牲了公平性，但获得了高吞吐量。在线程冲突较少的情况下，可以获得和CAS类似的性能；而线程冲突严重的情况下，性能远高于CAS。

![](./concurrent.png)


###
	对代码中的clear  reset  等重置，清除操作应该格外注意