package com.helun.menu.performance;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MergeSETaskFuture<T> extends FutureTask<T> {
	private static final Logger logger = LoggerFactory.getLogger(MergeSETaskFuture.class);
	private MergeSETask mergeSETask;
	public MergeSETaskFuture(Callable<T> callable) {
		super(callable);
		this.mergeSETask = (MergeSETask) callable;
	}

	
	/**
	 * future结束一定调用，无论正常还是异常结束
	 */
	@Override
	protected void done() {
		mergeSETask.clear();
	}

	/**
	 * 主动终止任务线程
	 * 
	 * @param mayInterruptIfRunning
	 * @return
	 */
	public Map<String, LXXResult> cancelMergeSETask(boolean mayInterruptIfRunning) {
		if (!mergeSETask.isCompleted()) {
			mergeSETask.setStatusAndResult(mergeSETask.CANCELLING, null);
		}
		if (mergeSETask.isCancelling()) {
			this.cancel(mayInterruptIfRunning);
			return mergeSETask.getResult();
		}
		return mergeSETask.getResult();
	}
}
