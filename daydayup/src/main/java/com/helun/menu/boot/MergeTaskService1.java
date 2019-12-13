package com.helun.menu.boot;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.helun.menu.api.MergeCEServiceLabel;
import com.helun.menu.api.TaskBOFactory;
import com.helun.menu.model.BaseEntity;
/**
 * 队列更新为非阻塞
 * 异常后任务cancel问题
 * @author 茗德
 *
 */
public class MergeTaskService1 {

	private static final Logger logger = LoggerFactory.getLogger(MergeTaskService1.class);
	private Integer excutePriod = 1000;
	private Integer queueCapatiy = 60;
	private MergeCEServiceLabel service = null;
	private TaskBOFactory factory = null;
	public Map<String, Queue<BaseEntity>> taskQueueMap = Maps.newConcurrentMap();
	public Map<String, FutureTask<Object>> futureQueueMap = Maps.newConcurrentMap();

	public MergeTaskService1(TaskBOFactory factory, MergeCEServiceLabel service, Integer excutePriod,
			Integer queueCapatiy) {
		if (factory == null || service == null) {
			throw new RuntimeException("TaskBOFactory and MergeCEServiceLabel can not be null.[TaskBOFactory:" + factory
					+ ",MergeCEServiceLabel:" + service + "]");
		}
		this.service = service;
		this.factory = factory;
		this.excutePriod = excutePriod == null ? 1000 : excutePriod;
		this.queueCapatiy = queueCapatiy == null ? 60 : queueCapatiy;
	}

	public FutureTask<Object> addTask(Long userId, Date applyTime, String... data) throws InterruptedException {

		BaseEntity entity = factory.buid(userId, applyTime, data);
		String queueKey = buildQueueKey(userId);
		Queue<BaseEntity> taskQueue = getTaskQueue(queueKey);
		FutureTask<Object> future = null;
		synchronized (taskQueue) {
			taskQueue.add(entity);
			future = getFuture(queueKey);
		}
		return future;
	}

	private String buildQueueKey(Long... ids) {
		if (ids == null || ids.length == 0) {
			throw new RuntimeException("ids is Null.");
		}
		String key = "";
		for (Long id : ids) {
			key = key + id;
		}
		return key;
	}

	private FutureTask<Object> getFuture(String queueKey) {
		FutureTask<Object> future = futureQueueMap.get(queueKey);
		if (future == null) {
			synchronized (futureQueueMap) {
				future = futureQueueMap.get(queueKey);
				if (future == null) {
					Callable<Object> applyCourseTask = new ApplyCourseTask(queueKey);
					future = new FutureTask<Object>(applyCourseTask);
					futureQueueMap.put(queueKey, future);
				}
			}
			new Thread(future).start();
		}
		return future;
	}

	private Queue<BaseEntity> getTaskQueue(String queueKey) {
		Queue<BaseEntity> taskQueue = taskQueueMap.get(queueKey);
		if (taskQueue == null) {
			synchronized (taskQueueMap) {
				taskQueue = taskQueueMap.get(queueKey);
				if (taskQueue == null) {
					taskQueue = new ConcurrentLinkedQueue<BaseEntity>();
					taskQueueMap.put(queueKey, taskQueue);
				}
			}
		}
		return taskQueue;
	}

	public int restFuture(String queueKey) {
		int length = 0;
		Queue<BaseEntity> taskQueue = taskQueueMap.get(queueKey);
		synchronized (taskQueue) {
			futureQueueMap.remove(queueKey);
			length = taskQueue.size();
		}
		return length;
	}

	public class ApplyCourseTask implements Callable<Object> {
		private String queueKey = null;

		public ApplyCourseTask(String queueKey) {
			this.queueKey = queueKey;
		}

		@Override
		public Object call() throws Exception {
			Thread.sleep(excutePriod);

			int length = restFuture(queueKey);
			Queue<BaseEntity> taskQueue = taskQueueMap.get(queueKey);
			List<BaseEntity> applyCourseTasks = Lists.newArrayList();

			for (int i = 0; i < length; i++) {
				BaseEntity task = taskQueue.poll();
				applyCourseTasks.add(task);
			}

			if (applyCourseTasks.size() > 0) {
				return service.servic(applyCourseTasks);
			}
			return 0;
		}
	}

}
