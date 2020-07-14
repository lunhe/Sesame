package com.helun.menu.performance;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;


public class MergeServiceProxy implements IMergeServiceProxy{

	IMergeSETaskAdapter mergeSETaskAdapter;

	public MergeServiceProxy(IMergeSETaskAdapter mergeSETaskAdapter) {
		this.mergeSETaskAdapter = mergeSETaskAdapter;
	}

	@Override
	public Map<String, LXXResult> proxyDoService(IMergeService mergerService , List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) {
		Map<Object,Object> context = Maps.newHashMap() ;
		return mergerService.doService(mergeSETaskBOs, currentTask,context);
	}

	@Override
	public Map<String, LXXResult> proxySaveService(IMergeService mergerService , List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) {
		Map<Object,Object> context = Maps.newHashMap() ;
		return this.saveServiceInProxy(mergerService , mergeSETaskBOs, currentTask,context) ;
	}

	@Override
	public Map<String, LXXResult> proxyDoAndSaveService(IMergeService mergerService,
			List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) {
		Map<Object,Object> context = Maps.newHashMap() ;
		mergerService.doService(mergeSETaskBOs, currentTask,context);
		if (Thread.currentThread().isInterrupted()) {
			throw new LXXRuntimeException("任务被取消");
		}
		Map<String, LXXResult> ret = this.saveServiceInProxy(mergerService , mergeSETaskBOs, currentTask,context) ;
		return ret;
	}
	
	@Override
	public Map<String, LXXResult> proxySaveAndDoService(IMergeService mergerService,
			List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) {
		Map<Object,Object> context = Maps.newHashMap() ;
		this.saveServiceInProxy(mergerService , mergeSETaskBOs, currentTask,context) ;
		if (Thread.currentThread().isInterrupted()) {
			throw new LXXRuntimeException("任务被取消");
		}
		return mergerService.doService(mergeSETaskBOs, currentTask,context);
	}
	
	@Override
	public Map<String, LXXResult> saveServiceInProxy(IMergeService mergerService ,List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask,Map<Object,Object> context) {
		Map<String, LXXResult> ret = mergerService.saveService(mergeSETaskBOs, currentTask,context) ;
		if (Thread.currentThread().isInterrupted()) {
			throw new LXXRuntimeException("任务被取消");
		}
		mergeSETaskAdapter.setStatusAndResult(currentTask, ret);
		return ret;
	}

	@Override
	public Map<String, LXXResult> proxy(Integer proxyType, IMergeService mergerService,
			List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) {
		if(JUST_NO_TRANSACTION.equals(proxyType)) {
			return this.proxyDoService(mergerService, mergeSETaskBOs, currentTask) ;  
		}
		if(FIRST_NONE_THEN_TRANSACTION.equals(proxyType)) {
			return this.proxyDoAndSaveService(mergerService, mergeSETaskBOs, currentTask) ;  
		}
		if(FIRST_TRANSACTION_THEN_NONE.equals(proxyType)) {
			return this.proxySaveAndDoService(mergerService, mergeSETaskBOs, currentTask) ;  
		}
		return this.proxySaveService(mergerService, mergeSETaskBOs, currentTask) ;  
	}

}
