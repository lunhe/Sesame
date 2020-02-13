package com.helun.menu.concurrent;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.helun.menu.api.MergeCEServiceLabel;
import com.helun.menu.api.TaskBOFactory;
import com.helun.menu.model.BaseEntity;

public class MergeTaskService {

	private static final Logger logger = LoggerFactory.getLogger(MergeTaskService.class);
	private Integer excutePriod = 1000;
	private Integer queueCapatiy = 60;
	private MergeCEServiceLabel service = null;
	private TaskBOFactory factory = null;
	private Map<String, Queue<BaseEntity>> taskQueueMap = Maps.newConcurrentMap();
	private Map<String, FutureTask<Object>> futureQueueMap = Maps.newConcurrentMap();

	public MergeTaskService(TaskBOFactory factory, MergeCEServiceLabel service, Integer excutePriod,
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
					Callable<Object> applyCourseTask = new CETask(queueKey);
					future = new CEFutureTask<Object>(applyCourseTask);
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
					taskQueue = new ArrayBlockingQueue<BaseEntity>(queueCapatiy);
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
	/**
	 * 任务执行结束后检查taskQueueMap中的队列是否为空，为空则删除，避免内存溢出
	 */
	private void clearTaskQueueMap(String queueKey) {
		Queue<BaseEntity> taskQueue = taskQueueMap.get(queueKey);

		synchronized (taskQueueMap) {
			taskQueue = taskQueueMap.get(queueKey);
			synchronized (taskQueue) {
				if (taskQueue != null && taskQueue.size() == 0) {
					taskQueueMap.remove(queueKey);
				}else {
					FutureTask<Object> futureTask = futureQueueMap.get(queueKey);
					if(futureTask == null) {
						taskQueueMap.remove(queueKey);
					}
				}
			}
		}
	}

	public class CEFutureTask<T> extends FutureTask<T>{
		private CETask applyCourseTask;
		public CEFutureTask(Callable<T> callable) {
			super(callable);
			this.applyCourseTask = (CETask)callable;
		}
		
		@Override
		protected void done() {
			applyCourseTask.clear();
			System.out.println(Thread.currentThread().getId() + "=================>"+ "线程结束，清理缓存");
		}
		
		
		/**
		 * 主动终止任务线程
		 * @param mayInterruptIfRunning
		 * @return
		 */
		 public Object cancelApplyCourse(boolean mayInterruptIfRunning) {
			 if(!applyCourseTask.isCompleted()) {
				 applyCourseTask.setStatusAndResult(applyCourseTask.CANCELLING,null);
			 }
			 if(applyCourseTask.isCancelling()) {
				 this.cancel(mayInterruptIfRunning);
				 return applyCourseTask.getResult();
			 }
			 return applyCourseTask.getResult();
		 }
		
	}
	
	
	public class CETask implements Callable<Object> {
		private String queueKey = null;
		
		public Integer CANCELLING = 1;
		public Integer COMPLETEED = 2;
		private Boolean isCancelling = false;
		private Boolean isCompleted = false;
		private Object result = null;
		

		public CETask(String queueKey) {
			this.queueKey = queueKey;
		}
		public void clear() {
			clearTaskQueueMap(queueKey);
		}
		
		public synchronized void setStatusAndResult(Integer status,Object result) {
			if(!this.isCancelling&&!this.isCompleted) {
				if(CANCELLING == status) {
					this.isCancelling = true ;
				}
				if(COMPLETEED == status) {
					this.isCompleted = true;
				}
				setResult(result);
			}
		}
		
		public void setResult(Object result) {
			this.result = result;
		}

		public Object getResult() {
			return this.result;
		}

		
		public Boolean isCancelling() {
			return this.isCancelling;
		}
		
		public Boolean isCompleted() {
			return this.isCompleted;
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
