package com.helun.menu.performance;

public interface IMergeSETaskBOBuilder {
	/**
	 * 
	 * 构建一个同一个入参形式
	 * @return
	 */
	public MergeSETaskBO build() ;
	/**
	 * 定义服务按照怎样的维度收集一个批次任务
	 * @return
	 */
	public String key() ;
	/**
	 * 定义处理当前批次的服务名称
	 * @return
	 */
	public String serviceName() ;
	
	/**
	 * 当前任务的代理类型 IMergeServiceProxy
	 * @return
	 */
	public Integer proxyType() ;
}
