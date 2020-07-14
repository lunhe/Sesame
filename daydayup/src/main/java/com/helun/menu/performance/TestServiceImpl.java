package com.helun.menu.performance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.google.common.collect.Maps;

public class TestServiceImpl implements ITestService, IMergeService {
	IMergeSETaskAdapter mergeSETaskAdapter;

	public TestServiceImpl(IMergeSETaskAdapter mergeSETaskAdapter) {
		mergeSETaskAdapter.registerService("test", this);
		this.mergeSETaskAdapter = mergeSETaskAdapter;
	}

	public Map<String, LXXResult> submit() {
		// 非事务处理
		Map<String, LXXResult> ret = Maps.newHashMap();
		Long i = 5000000L;
		while (i-- > 0) {
			Double d = i * 9.3333;
		}
		if (Thread.currentThread().isInterrupted()) {
			throw new LXXRuntimeException("任务被取消");
		}
		ret.put("read", new LXXResult()) ;
		return ret ;
	}

	@Override
	public Map<String, LXXResult> saveTest(List<MergeSETaskBO> mergeSETaskBOs) {
		Map<String, LXXResult> ret = Maps.newHashMap();
		for (MergeSETaskBO mergeSETaskBO : mergeSETaskBOs) {
			Long id = (Long) mergeSETaskBO.getProperty("id");
			String name = (String) mergeSETaskBO.getProperty("name");
			System.out.println(Thread.currentThread().getName() + " do service for " + name);
			Long i = 50000000L;
			//Long i = 500000000L;
			while (i-- > 0) {
				Double d = i * 9.3333;
			}
			mergeSETaskBO.addProperty("age", Thread.currentThread().getName()) ;
			LXXResult lxxResult = new LXXResult();
			lxxResult.setResult(mergeSETaskBO);
			ret.put(id.toString(), lxxResult);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, LXXResult> saveService(List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask,Map<Object,Object> context) {
		Map<String, LXXResult> ret1 = (Map<String, LXXResult>) context.get("ret") ;
		Map<String, LXXResult> ret2 = saveTest(mergeSETaskBOs);
		ret1.putAll(ret2);
		mergeSETaskAdapter.setStatusAndResult(currentTask, ret1);
		return ret2;
	}

	@Override
	public Map<String, LXXResult> doService(List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask,Map<Object,Object> context) {
		Map<String, LXXResult> ret = submit() ;
		context.put("ret", ret) ;
		return ret;
	}

}
