package com.helun.menu.performance;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("mergeSETaskAdapter")
public class MergeSETaskAdapter implements IMergeSETaskAdapter {

	private static final Logger logger = LoggerFactory.getLogger(MergeSETaskAdapter.class);

//	@Resource(name = "loginService")
//	private ILoginService loginService;
	
//	@Resource(name = "mergerServiceProxy")
//	private IMergerServiceProxy mergerServiceProxy ;

	private Map<String, Queue<MergeSETaskBO>> taskQueueMap = Maps.newConcurrentMap();
	private Map<String, MergeSETaskFuture<Map<String, LXXResult>>> futureQueueMap = Maps.newConcurrentMap();
	private Map<String, IMergeService> serviceMap = Maps.newHashMap();

	@Override
	public MergeSETaskFuture<Map<String, LXXResult>> addTask(LXXLoginInfo loginInfo,
			IMergeSETaskBOBuilder mergeSETaskBOBuilder) {
		// 构建数据模型
		MergeSETaskBO mergeSETaskBO = mergeSETaskBOBuilder.build();
		if (mergeSETaskBO == null) {
			throw new RuntimeException("Invalid task.");
		}
		// 注册的服务对象名称
		String serviceName = mergeSETaskBOBuilder.serviceName();
		if (StringUtils.isBlank(serviceName)) {
			throw new RuntimeException("Invalid serviceName.");
		}
		// 代理的方式，默认全部业务都在事务中
		Integer proxyType = mergeSETaskBOBuilder.proxyType() ;
		if(proxyType == null) {
			proxyType = IMergeServiceProxy.JUST_TRANSACTION ;
		}
		// 本次任务的key，定义了按照怎样的维度去划分提交批次
		String key = mergeSETaskBOBuilder.key();
		// 防止服务之间出现相同的key
		String queueKey = serviceName + key;
		// 获取任务队列
		Queue<MergeSETaskBO> taskQueue = getTaskQueue(queueKey);
		// 将任务投递到队列中并获取这一批次的future
		MergeSETaskFuture<Map<String, LXXResult>> future = null;
		// 锁住该队列，防止此时其他线程执行restFuture(),计算队列长度
		synchronized (taskQueue) {
			taskQueue.add(mergeSETaskBO);
			future = getFuture(loginInfo, serviceName,proxyType , queueKey);
		}
		return future;
	}

	/**
	 * 获取一个和班级，测试关联的Future
	 * 
	 * @param loginInfo
	 * @param queueKey
	 * @return
	 */
	private MergeSETaskFuture<Map<String, LXXResult>> getFuture(LXXLoginInfo loginInfo, String beanName,
			Integer proxyType , String queueKey) {
		MergeSETaskFuture<Map<String, LXXResult>> future = futureQueueMap.get(queueKey);
		if (future == null) {
			synchronized (futureQueueMap) {
				future = futureQueueMap.get(queueKey);
				if (future == null) {
					Callable<Map<String, LXXResult>> mergeSETask = new MergeSETask(loginInfo, beanName, proxyType , queueKey, this);
					future = new MergeSETaskFuture<Map<String, LXXResult>>(mergeSETask);
					futureQueueMap.put(queueKey, future);
				}
			}
			Thread taskThead = new Thread(future);
			taskThead.start();
		}
		return future;
	}

	/**
	 * 获取任务队列
	 * 
	 * @param queueKey
	 * @return
	 */
	private Queue<MergeSETaskBO> getTaskQueue(String queueKey) {
		Queue<MergeSETaskBO> taskQueue = taskQueueMap.get(queueKey);
		if (taskQueue == null) {
			synchronized (taskQueueMap) {
				taskQueue = taskQueueMap.get(queueKey);
				if (taskQueue == null) {
					taskQueue = new ConcurrentLinkedQueue<MergeSETaskBO>();
					taskQueueMap.put(queueKey, taskQueue);
				}
			}
		}
		return taskQueue;
	}

	/**
	 * @param queueKey
	 * @return 队列中当前future需要处理的任务数
	 */
	public List<MergeSETaskBO> restFuture(String queueKey) {
		int length = 0;
		List<MergeSETaskBO> mergeSETaskBOs = Lists.newArrayList();
		Queue<MergeSETaskBO> taskQueue = taskQueueMap.get(queueKey);
		if (taskQueue != null) {
			// 锁住该队列，将队列作为互斥锁，移除将要执行的future，并为该future计算需要处理的任务
			synchronized (taskQueue) {
				futureQueueMap.remove(queueKey);
				length = taskQueue.size();
				for (int i = 0; i < length; i++) {
					MergeSETaskBO task = taskQueue.poll();
					if (task != null) {
						mergeSETaskBOs.add(task);
					}
				}
			}
		}
		return mergeSETaskBOs;
	}

	/**
	 * 任务执行结束后检查taskQueueMap中的队列是否为空，为空则删除，避免内存溢出
	 * 
	 * @param queueKey
	 */
	public void clearTaskQueueMap(String queueKey) {
		Queue<MergeSETaskBO> taskQueue = taskQueueMap.get(queueKey);
		if (taskQueue != null && taskQueue.size() == 0) {
			synchronized (taskQueueMap) {
				taskQueue = taskQueueMap.get(queueKey);
				if (taskQueue != null) {
					synchronized (taskQueue) {
						// 任务队列存在且为空，正常移除
						if (taskQueue.size() == 0) {
							taskQueueMap.remove(queueKey);
						} else if (taskQueue.size() > 0) {
							// 任务队列不为空，检查是否存在对应的future，不存在则移除队列(这其实是一种异常情况，会造成任务丢失)
							FutureTask<Map<String, LXXResult>> futureTask = futureQueueMap.get(queueKey);
							if (futureTask == null) {
								taskQueueMap.remove(queueKey);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Integer registerService(String serviceName, IMergeService mergerService) {
		try {
			if (serviceMap.get(serviceName) == null) {
				serviceMap.put(serviceName, mergerService);
				return RGSUCCESS;
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return RGFAIL;
		}
		return RGED;
	}

	@Override
	public IMergeService loadService(String serviceName) {
		if (StringUtils.isNotBlank(serviceName)) {
			return serviceMap.get(serviceName);
		}
		throw new RuntimeException("Invalid serviceName on loading.");
	}

	@Override
	public IMergeService unRegisterService(String serviceName) {
		return serviceMap.remove(serviceName);
	}

	@Override
	public void registerloginInfo(LXXLoginInfo loginInfo) {
		// loginService.setLoginInfo(loginInfo);
	}

	@Override
	public LXXResult waitResult(MergeSETaskFuture<Map<String, LXXResult>> future, Long waitTime, String resutlKey) {
		Map<String, LXXResult> ret = null;
		try {
			ret = future.get(waitTime, TimeUnit.SECONDS);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (CancellationException | TimeoutException | InterruptedException e) {
			Boolean isCanceled = future.isCancelled();
			if (!isCanceled) {
				synchronized (future) {
					if (!future.isCancelled()) {
						ret = future.cancelMergeSETask(true);
					}
				}
			}
		}
		if (ret != null) {
			return ret.get(resutlKey);
		} else {
			LXXResult lxxResult = new LXXResult();
			lxxResult.setMsg("提交答案超时。");
			return lxxResult;
		}
	}

	@Override
	public void setStatusAndResult(MergeSETask currentTask, Map<String, LXXResult> ret) {
		if (!currentTask.isCancelling()) {
			currentTask.setStatusAndResult(currentTask.COMPLETEED, ret);
		}
		if (currentTask.isCancelling()) {
			throw new LXXRuntimeException("任务被取消");
		}
	}

	@Override
	public IMergeServiceProxy getMergeServiceProxy() {
		MergeServiceProxy mergeServiceProxy = new MergeServiceProxy(this);
		if(mergeServiceProxy != null) {
			return mergeServiceProxy ;
		}
		throw new RuntimeException("Invalid IMergeServiceProxy.");
	}

}
