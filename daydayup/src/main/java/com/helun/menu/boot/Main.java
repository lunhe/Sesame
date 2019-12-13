package com.helun.menu.boot;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.helun.menu.api.MergeCEServiceLabel;
import com.helun.menu.api.TaskBOFactory;
import com.helun.menu.model.BaseEntity;
import com.helun.menu.util.FileLoader;

public class Main {
	private static MergeTaskService mergeTaskService ;
	public static void main(String[] args) {
		System.out.println("------------------0");
		mergeTaskService = new MergeTaskService(new TaskBOFactory() {
			
			@Override
			public BaseEntity buid(Long userId, Date applyTime, String... data) {
				// TODO Auto-generated method stub
				return new BaseEntity();
			}
		}, new MergeCEServiceLabel() {
			
			@Override
			public Object servic(Collection<BaseEntity> taskBos) {
				System.out.println("begin  "+Thread.currentThread().getId() + " ======>" + taskBos.size());
				Long start = System.currentTimeMillis();
				for(int i = 0 ; i < 10000 ; i++) {
					for(int j = 0 ; j < 10000 ; j++) {
						if(i%2000==0&&j%2000==0){
							try {
								Thread.sleep(600);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("do something   i:"+i+ "  j:" + j);
						}
					}
				}
				Long end = System.currentTimeMillis();
				System.out.println("finish  "+Thread.currentThread().getId() + " ======> "+ (end-start)+"ms");
				return 0;
			}
		}, 500, 60);
		System.out.println("------------------1");
		for(int i = 0 ; i< 55 ;i ++) {
			demoClient(i);
		}
		System.out.println("------------------2");
		
	}
	
	
	public static void demoClient(int i) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
				FutureTask<Object> future = null;
				try {
					future = mergeTaskService.addTask(1000L, new Date(), "test");
					//future.get();
					future.get(5, TimeUnit.SECONDS);
				} catch (TimeoutException e) {
					e.printStackTrace();
					ArrayBlockingQueue<BaseEntity> queue = mergeTaskService.taskQueueMap.get("1000");
					System.out.println(Thread.currentThread().getId() + "===" +"queue size :   "  + queue.size());
					System.out.println(Thread.currentThread().getId() + "===" +mergeTaskService.futureQueueMap);
					System.out.println(Thread.currentThread().getId() + "===" +"cancelled:    " + future.isCancelled());
					System.out.println(Thread.currentThread().getId() + "===" +"isdone:   "+future.isDone());
					Boolean isCanceled = future.cancel(true);
					if(isCanceled) {
						System.out.println(Thread.currentThread().getId() + "===" +"提交答案超时");
					}else {
						if(future.isDone()) {
							System.out.println(Thread.currentThread().getId() + "===" +"提交答案完成");
						}else{
							System.out.println(Thread.currentThread().getId() + "===" +"取消任务失败");
						}
					}
				}
				}catch(Exception e){
					System.out.println("null");
				}
			}
		}).start();
	}

	public static void testMenu() {
		List<String> menus = FileLoader.readFileByLineOnResources("src/main/resources/menu.json");
		Random random = new Random();
		if (menus.size() > 0) {
			for (int i = 0; i < 3; i++) {
				System.out.println(random.nextInt(menus.size()));
				System.out.println(menus.get(random.nextInt(menus.size())));
			}
		}
	}
}
