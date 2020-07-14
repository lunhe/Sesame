package com.helun.menu.performance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergeSETask implements Callable<Map<String, LXXResult>> {

	public Integer CANCELLING = 1;
	public Integer COMPLETEED = 2;
	private Boolean isCancelling = false;
	private Boolean isCompleted = false;

	private static final Logger logger = LoggerFactory.getLogger(MergeSETask.class);

	private IMergeSETaskAdapter mergeSETaskAdapter = null;
	private String queueKey = null;
	private String serviceName = null;
	private LXXLoginInfo loginInfo = null;
	private Integer proxyType = IMergeServiceProxy.JUST_TRANSACTION ;
	private Map<String, LXXResult> result = null;

	public MergeSETask(LXXLoginInfo loginInfo, String serviceName, Integer proxyType ,String queueKey,
			IMergeSETaskAdapter mergeSETaskAdapter) {
		if (mergeSETaskAdapter == null || loginInfo == null || StringUtils.isBlank(queueKey)
				|| StringUtils.isBlank(serviceName)) {
			throw new LXXRuntimeException(
					"The mergeSETaskAdapter or serviceName or loginInfo or queueKey is invalid.#[loginInfo:" + loginInfo
							+ ",queueKey:" + queueKey + ",serviceName:" + serviceName + ",mergeSETaskAdapter:"
							+ mergeSETaskAdapter + "]");
		}

		this.mergeSETaskAdapter = mergeSETaskAdapter;
		this.loginInfo = loginInfo;
		this.queueKey = queueKey;
		this.proxyType = proxyType ;
		this.serviceName = serviceName;
	}

	@Override
	public Map<String, LXXResult> call() {
		try {
			mergeSETaskAdapter.registerloginInfo(loginInfo);
			
			Integer priod = 500;
			try {
				// priod = Integer.parseInt(apply_task_priod) ;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			Thread.sleep(priod);
			IMergeServiceProxy mergeServiceProxy = mergeSETaskAdapter.getMergeServiceProxy() ;
			IMergeService mergerService = mergeSETaskAdapter.loadService(serviceName);
			List<MergeSETaskBO> mergeSETaskBOs = mergeSETaskAdapter.restFuture(queueKey);

			if (mergeSETaskBOs.size() > 0) {
				Map<String, LXXResult> ret = mergeServiceProxy.proxy(proxyType, mergerService, mergeSETaskBOs, this) ;
				return ret;
			}
			throw new LXXRuntimeException("任务异常，当前线程任务队列为空");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new LXXRuntimeException(e);
		} finally {
			clear();
		}
	}

	public void clear() {
		mergeSETaskAdapter.clearTaskQueueMap(queueKey);
	}

	public synchronized void setStatusAndResult(Integer status, Map<String, LXXResult> result) {
		if (!this.isCancelling && !this.isCompleted) {
			if (CANCELLING == status) {
				this.isCancelling = true;
			}
			if (COMPLETEED == status) {
				this.isCompleted = true;
			}
			setResult(result);
		}
	}

	public void setResult(Map<String, LXXResult> result) {
		this.result = result;
	}

	public Map<String, LXXResult> getResult() {
		return this.result;
	}

	public Boolean isCancelling() {
		return this.isCancelling;
	}

	public Boolean isCompleted() {
		return this.isCompleted;
	}

}
