package com.helun.menu.performance;

import java.util.List;
import java.util.Map;
/**
 * This is an proxy for server service.</br>
 * @version 1.0.0.0
 * @Date 2020-07-03
 * @Desc
 * There are four kinds of proxy type.</br>
 * <li>1({@link JUST_NO_TRANSACTION}) : Just execute the server service without transaction </li>
 * <li>2({@link JUST_TRANSCATION}) : Just execute the server service with transaction </li>
 * <li>4({@link FIRST_NONE_THEN_TRANSCATION}) : When execute the server service , first no transaction and then transaction </li>
 * <li>8({@link FIRST_TRANSCATION_THEN_NONE}) : When execute the server service , first transaction and then no transaction </li>
 * @see com.helun.menu.performance.IMergeServiceProxy
 * @author helun
 *
 */
public interface IMergeServiceProxy {
	/**
	 * 代理层只有非事务
	 * @see JUST_NO_TRANSACTION
	 */
	public Integer JUST_NO_TRANSACTION = 1 ;
	/**
	 * 代理层只有事务
	 * @see JUST_TRANSACTION
	 */
	public Integer JUST_TRANSACTION = 2 ;
	/**
	 * 代理层先非事务，再事务
	 * @see FIRST_NONE_THEN_TRANSACTION
	 */
	public Integer FIRST_NONE_THEN_TRANSACTION = 4 ;
	/**
	 * 代理层先事务，再非事务
	 * @see FIRST_TRANSACTION_THEN_NONE
	 */
	public Integer FIRST_TRANSACTION_THEN_NONE = 8 ;
	
	/**
	 * 代理业务
	 * @param proxyType 代理层的事务处理方式 {@link com.helun.menu.performance.IMergeServiceProxy}
	 * @param mergerService 业务服务对象
	 * @param mergeSETaskBOs 业务数据
	 * @param currentTask 当前任务
	 * @return
	 */
	public Map<String, LXXResult> proxy(Integer proxyType ,IMergeService mergerService , List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) ;
	
	/**
	 * 代理只有非事务的业务
	 * @param mergerService 业务服务对象
	 * @param mergeSETaskBOs 业务数据
	 * @param currentTask 当前任务
	 * @return
	 */
	public Map<String, LXXResult> proxyDoService(IMergeService mergerService , List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) ;
	/**
	 * 代理只有事务的业务
	 * @param mergerService 业务服务对象
	 * @param mergeSETaskBOs 业务数据
	 * @param currentTask 当前任务
	 * @return
	 */
	public Map<String, LXXResult> proxySaveService(IMergeService mergerService , List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) ;
	/**
	 * 代理先非事务然后事务的业务
	 * @param mergerService 业务服务对象
	 * @param mergeSETaskBOs 业务数据
	 * @param currentTask 当前任务
	 * @return
	 */
	public Map<String, LXXResult> proxyDoAndSaveService(IMergeService mergerService , List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) ;
	/**
	 * 代理先事务然后非事务的业务
	 * @param mergerService 业务服务对象
	 * @param mergeSETaskBOs 业务数据
	 * @param currentTask 当前任务
	 * @return
	 */
	public Map<String, LXXResult> proxySaveAndDoService(IMergeService mergerService , List<MergeSETaskBO> mergeSETaskBOs, MergeSETask currentTask) ;
	
	/**
	 * 将业务逻辑和同步逻辑处理在一个事务之中
	 * @param mergerService
	 * @param mergeSETaskBOs
	 * @param currentTask
	 * @param context
	 * @return
	 */
	public Map<String, LXXResult> saveServiceInProxy(IMergeService mergerService ,List<MergeSETaskBO> mergeSETaskBOs , MergeSETask currentTask,Map<Object,Object> context) ;
}
