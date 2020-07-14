package com.helun.menu.performance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

public interface IMergeSETaskAdapter {
	/**
	 * 注册失败
	 */
	Integer RGFAIL = 0 ;
	/**
	 * 注册成功
	 */
	Integer RGSUCCESS = 1 ;
	/**
	 * 已注册（重复注册）
	 */
	Integer RGED = 2 ;
	
	/**
	 * 添加新的任务
	 * @param loginInfo
	 * @param mergeSETaskBOBuilder
	 * @return
	 */
	public MergeSETaskFuture<Map<String, LXXResult>> addTask(LXXLoginInfo loginInfo, IMergeSETaskBOBuilder mergeSETaskBOBuilder) ;
	
	/**
	 * 清除已经消费的任务队列
	 * @param queueKey
	 */
	public void clearTaskQueueMap(String queueKey) ;
	
	/**
	 * 重置future: 一个查询批次开始时，重置缓存中关联的future，并计算将要执行的future需要处理的提交任务
	 * @param queueKey
	 * @return
	 */
	public List<MergeSETaskBO> restFuture(String queueKey) ;
	
	/**
	 * 注册服务
	 * @param serviceName
	 * @param mergerService
	 * @return
	 */
	public Integer registerService(String serviceName ,IMergeService mergerService) ;
	
	/**
	 * 注销服务
	 * @param serviceName
	 * @return
	 */
	public IMergeService unRegisterService(String serviceName) ;
	
	/**
	 * 加载服务
	 * @param serviceName
	 * @return not null
	 */
	public IMergeService loadService(String serviceName) ;
	
	/**
	 * 向当前线程注册登录信息
	 * @param loginInfo
	 */
	public void registerloginInfo(LXXLoginInfo loginInfo) ;
	
	/**
	 * 等待future返回
	 * @param future
	 * @param waitTime
	 * @param resutlKey
	 * @return
	 */
	public LXXResult waitResult(MergeSETaskFuture<Map<String, LXXResult>> future , Long waitTime, String resutlKey) ;
	/**
	 * 检查任务状态和设置任务结果
	 * @param currentTask
	 */
	public void setStatusAndResult(MergeSETask currentTask,Map<String, LXXResult> ret) ;
	/**
	 * 检查任务状态和设置任务结果
	 * @return not null
	 */
	public IMergeServiceProxy getMergeServiceProxy() ;
}
