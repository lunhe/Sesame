package com.helun.menu.boot;

import java.util.Collection;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.helun.menu.api.MergeCEServiceLabel;
import com.helun.menu.api.TaskBOFactory;
import com.helun.menu.model.BaseEntity;

public class Main2 {

	
	private static MergeTaskService2 mergeTaskService ;
	public static void main(String[] args) {
		System.out.println("------------------0");
		mergeTaskService = new MergeTaskService2(new TaskBOFactory() {
			
			@Override
			public BaseEntity buid(Long userId, Date applyTime, String... data) {
				return new BaseEntity();
			}
		}, new MergeCEServiceLabel() {
			
			private void isInterrupted() {
				if(Thread.interrupted()) {
					throw new RuntimeException("任务被中断");
				}
			}
			
			@Override
			public Object servic(Collection<BaseEntity> taskBos) {
				System.out.println("begin  "+System.currentTimeMillis() + "  "+ Thread.currentThread().getId() + " ======>" + taskBos.size());
				Long start = System.currentTimeMillis();
				try {
				for(int i = 0 ; i < 10000 ; i++) {
					for(int j = 0 ; j < 2000*taskBos.size() ; j++) {
						if(i%2000==0&&j%2000==0){
								isInterrupted();
								Thread.sleep(30);
						}
					}
				}
				} catch (InterruptedException e) {
					throw new RuntimeException( "任务被中断");
				}
				Long end = System.currentTimeMillis();
				System.out.println("finish  "+Thread.currentThread().getId() + " ======> "+ (end-start)+"ms");
				return 0;
			}
		}, 1000, 20);
		System.out.println("------------------1");
		for(int i = 0 ; i< 120 ;i ++) {
			demoClient(i);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
		System.out.println("------------------2");
		
	}	
	
	
	public static void demoClient(int i) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					Object ret = 0 ;
				FutureTask<Object> future = null;
				try {
					future = mergeTaskService.addTask(1000L, new Date(), "test");
					ret = future.get(5, TimeUnit.SECONDS);
				}catch(ExecutionException e){
					ret = -1;
					throw new RuntimeException("提交答案异常,答题已取消。"+ret);
				}catch (TimeoutException | CancellationException | InterruptedException e) {
					Boolean isCanceled = future.isCancelled();
					if(!isCanceled){
						synchronized (future) {
							if(!future.isCancelled()){
								isCanceled = future.cancel(true);
								if(!isCanceled){
									throw new RuntimeException("提交答案超时,答题取消失败。");
								}
							}	
						}
					}
					ret = -2;
					throw new RuntimeException("提交答案超时,答题已取消。"+ret);
				}
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
		}).start();
	}
}


